package cl.cns.integracion.wsreferidosrrvv.clases;

import cl.cns.integracion.wsreferidosrrvv.controller.AccionesJpaController;
import cl.cns.integracion.wsreferidosrrvv.controller.BitacorasJpaController;
import cl.cns.integracion.wsreferidosrrvv.controller.ReferenciasJpaController;
import cl.cnsv.referidosrrvv.controller.exceptions.NonexistentEntityException;
import cl.cnsv.referidosrrvv.controller.exceptions.RollbackFailureException;
import cl.cnsv.referidosrrvv.models.Acciones;
import cl.cnsv.referidosrrvv.models.Bitacoras;
import cl.cnsv.referidosrrvv.models.Referencias;
import jakarta.persistence.EntityManager;

import java.math.BigInteger;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ActualizarDatosReferencias {

    private static final Logger LOGGER =
            LogManager.getLogger(ActualizarDatosReferencias.class);

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("EEE, d MMM yyyy");

    public List<ErrorActualizaDatosReferenciasOut> actualizarSinUso(
            List<EntidadDeCargaJs> entity,
            EntityManager em)
            throws NonexistentEntityException, RollbackFailureException,
            cl.cns.integracion.wsreferidosrrvv.exceptions.IllegalOrphanException,
            cl.cns.integracion.wsreferidosrrvv.exceptions.NonexistentEntityException,
            cl.cns.integracion.wsreferidosrrvv.exceptions.RollbackFailureException,
            cl.cns.integracion.wsreferidosrrvv.exceptions.PreexistingEntityException {

        ReferenciasJpaController referenciasController = new ReferenciasJpaController(em);
        BitacorasJpaController bc = new BitacorasJpaController(em);
        AccionesJpaController ac = new AccionesJpaController(em);

        List<ErrorActualizaDatosReferenciasOut> eadro = new ArrayList<>();

        for (EntidadDeCargaJs e : entity) {

            if (e.getID() == null) continue;

            try {
                Referencias referencia = referenciasController.findReferencias(e.getID());

                if (referencia == null) {
                    throw new NullPointerException("ID no existe en base de datos");
                }

                continuar4(referencia, referenciasController, e, bc);
                continuar3(referencia, ac, referenciasController, e, bc);
                continuar2(referencia, e, referenciasController, bc);

            } catch (Exception ex) {
                continuar(ex, e, eadro);
            }
        }

        return eadro;
    }

    private void continuar(
            Exception ex,
            EntidadDeCargaJs e,
            List<ErrorActualizaDatosReferenciasOut> eadro) {

        String error = "*** Error ID -> "
                + (e.getID() != null ? e.getID() : "NULL")
                + " -> "
                + ex.getMessage();

        ErrorActualizaDatosReferenciasOut out = new ErrorActualizaDatosReferenciasOut();
        out.setId(e.getID() != null ? e.getID().toString() : "NULL");
        out.setEstado(e.getESTADO());
        out.setCanal(e.getCANAL());
        out.setError(error);

        eadro.add(out);

        LOGGER.warn(error);
    }

    private void continuar2(
            Referencias referencia,
            EntidadDeCargaJs e,
            ReferenciasJpaController referenciasController,
            BitacorasJpaController bc)
            throws Exception {

        if (e.getFECHA_RECEPCION() == null) return;

        Date fechaOld = referencia.getFecha();
        Date nuevaFecha = e.getFECHA_RECEPCION();

        referencia.setFecha(nuevaFecha);
        referenciasController.edit(referencia);

        Bitacoras b = new Bitacoras();
        b.setComentarios(
                "CAMBIO FECHA: "
                        + format(fechaOld)
                        + " -> "
                        + format(nuevaFecha)
        );
        b.setFecha(new Date());
        b.setReferenciaId(referencia);
        b.setVersion(BigInteger.ONE);
        b.setUsuario(e.getUSUARIO());

        bc.create(b);
    }

    private void continuar3(
            Referencias referencia,
            AccionesJpaController ac,
            ReferenciasJpaController referenciasController,
            EntidadDeCargaJs e,
            BitacorasJpaController bc)
            throws Exception {

        if (e.getESTADO() == null || e.getESTADO().isEmpty()) return;

        String estadoOld = (referencia.getAccionId() != null
                && referencia.getAccionId().getNombre() != null)
                ? referencia.getAccionId().getNombre()
                : "SIN_ESTADO";

        Acciones nuevoEstado = ac.findByNombre(e.getESTADO());

        String estadoNuevo = (nuevoEstado != null && nuevoEstado.getNombre() != null)
                ? nuevoEstado.getNombre()
                : "NULL";

        referencia.setAccionId(nuevoEstado);
        referenciasController.edit(referencia);

        Bitacoras b = new Bitacoras();
        b.setComentarios(
                "CAMBIO ESTADO: "
                        + estadoOld.toUpperCase()
                        + " -> "
                        + estadoNuevo.toUpperCase()
        );
        b.setFecha(new Date());
        b.setReferenciaId(referencia);
        b.setVersion(BigInteger.ONE);
        b.setUsuario(e.getUSUARIO());

        bc.create(b);
    }

    private void continuar4(
            Referencias referencia,
            ReferenciasJpaController referenciasController,
            EntidadDeCargaJs e,
            BitacorasJpaController bc)
            throws Exception {

        if (e.getCANAL() == null || e.getCANAL().isEmpty()) return;

        String canalOld = referencia.getCanalname() != null
                ? referencia.getCanalname()
                : "";

        referencia.setCanalname(e.getCANAL());
        referenciasController.edit(referencia);

        Bitacoras b = new Bitacoras();
        b.setComentarios(
                "CAMBIO CANAL: "
                        + canalOld.toUpperCase()
                        + " -> "
                        + e.getCANAL().toUpperCase()
        );
        b.setFecha(new Date());
        b.setReferenciaId(referencia);
        b.setVersion(BigInteger.ONE);
        b.setUsuario(e.getUSUARIO());

        bc.create(b);
    }

    private String format(Date date) {
        if (date == null) return "NULL";
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(FORMATTER);
    }

    public static class ErrorActualizaDatosReferenciasOut {

        private String id;
        private String canal;
        private String estado;
        private String error;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getCanal() { return canal; }
        public void setCanal(String canal) { this.canal = canal; }

        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }

        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }
}