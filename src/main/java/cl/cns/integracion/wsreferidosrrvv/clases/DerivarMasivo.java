package cl.cns.integracion.wsreferidosrrvv.clases;

import cl.cns.integracion.wsreferidosrrvv.controller.AccionesJpaController;
import cl.cns.integracion.wsreferidosrrvv.controller.BitacorasJpaController;
import cl.cns.integracion.wsreferidosrrvv.controller.EjecutivosJpaController;
import cl.cns.integracion.wsreferidosrrvv.controller.ReferenciasJpaController;
import cl.cns.integracion.wsreferidosrrvv.exceptions.RollbackFailureException;
import cl.cns.integracion.wsreferidosrrvv.util.construirBodyEnviarMail;
import cl.cnsv.referidosrrvv.models.Acciones;
import cl.cnsv.referidosrrvv.models.Bitacoras;
import cl.cnsv.referidosrrvv.models.Ejecutivos;
import cl.cnsv.referidosrrvv.models.Referencias;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.transaction.UserTransaction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DerivarMasivo {

    private static final String UNDEFINED = "undefined";

    private static final Logger LOGGER =
            LogManager.getLogger(DerivarMasivo.class);

    public void derivar(
            String suc,
            String anio,
            String mes,
            String sup,
            String usr,
            String id,
            String eje,
            UserTransaction utx,
            EntityManager em) throws RollbackFailureException, Exception {

        EjecutivosJpaController ejc = new EjecutivosJpaController(em);

        Ejecutivos ejecutivo = null;

        // =========================
        // OBTENER EJECUTIVO
        // =========================
        if (eje != null && !eje.isBlank()) {
            try {
                ejecutivo = ejc.findEjecutivos(
                        BigDecimal.valueOf(Long.parseLong(eje))
                );
            } catch (Exception ex) {
                LOGGER.error("Error obteniendo ejecutivo", ex);
            }
        }

        // =========================
        // ARMADO SQL
        // =========================
        String sql = Resources.sqlDerivarMasivoDetalExportarDataTable();

        String search = (id != null) ? id.replace(" ", "%") : "";
        sql = sql.replace("?1", search);

        // SUPERVISOR
        String supParam = (sup != null) ? sup : UNDEFINED;
        supParam = (!UNDEFINED.equals(supParam))
                ? " AND OWNERE IN (" + supParam + ")"
                : "";
        sql = sql.replace("?6", supParam);

        // FECHA
        String valorAnioMes = "";

        if (anio != null && !UNDEFINED.equals(anio)) {
            String cExtract = " EXTRACT(year FROM FECHA) = " + anio;

            if (mes != null && !UNDEFINED.equals(mes)) {
                cExtract += " AND EXTRACT(month FROM FECHA) = " + mes;
            }

            valorAnioMes = " AND " + cExtract;
        }

        sql = sql.replace("?7", valorAnioMes);

        // SUCURSAL
        String valorSucursal = "";

        if (suc != null && !UNDEFINED.equals(suc)) {
            valorSucursal = " AND sucursal_id=" + suc;
        }

        sql = sql.replace("?8", valorSucursal);

        // =========================
        // QUERY
        // =========================
        Query q = em.createNativeQuery(sql, Referencias.class);
        List<Referencias> result = new LinkedList<>(q.getResultList());

        if (result.isEmpty()) {
            LOGGER.info("No hay registros para procesar");
            return;
        }

        ReferenciasJpaController rc = new ReferenciasJpaController(em);
        BitacorasJpaController bc = new BitacorasJpaController(em);
        AccionesJpaController ac = new AccionesJpaController(em);

        Acciones accion = ac.findAcciones(new BigDecimal("5"));

        // =========================
        // PROCESO MASIVO
        // =========================
        for (Referencias r : result) {

            r.setAccionId(accion);

            if (ejecutivo != null) {
                r.setEjecutivoId(ejecutivo);
                r.setOwnere(ejecutivo.getCodigo());
                r.setOwnerename(ejecutivo.getNombre());
            }

            rc.edit(r);

            Bitacoras b = new Bitacoras();
            b.setComentarios(
                    "REFERENCIA DERIVADA EN PROCESO MASIVO A EJECUTIVO -> "
                            + (ejecutivo != null ? ejecutivo.getNombre() : "SIN_EJECUTIVO")
            );
            b.setFecha(new Date());
            b.setReferenciaId(r);
            b.setUsuario(usr);
            b.setVersion(BigInteger.ONE);

            bc.create(b);

            // =========================
            // CORREO INDIVIDUAL
            // =========================
            if (ejecutivo != null
                    && result.size() <= Resources.CANT_CORREOS_MAX_POR_EJECUTIVA) {

                try {
                    construirBodyEnviarMail.construir(
                            ejecutivo.getCorreo(),
                            r.getReferidoId() != null ? r.getReferidoId().getRut() : null,
                            r.getReferidoId() != null ? r.getReferidoId().getNombre() : null,
                            r.getReferidoId() != null ? r.getReferidoId().getRegion() : null,
                            r.getReferidoId() != null ? r.getReferidoId().getComuna() : null,
                            r.getCanalname(),
                            result.size()
                    );
                } catch (NoResultException ex) {
                    LOGGER.info("No result al enviar correo individual", ex);
                } catch (Exception ex) {
                    LOGGER.error("Error enviando correo individual", ex);
                }
            }
        }

        // =========================
        // CORREO MASIVO (FUERA DEL LOOP)
        // =========================
        if (ejecutivo != null
                && result.size() > Resources.CANT_CORREOS_MAX_POR_EJECUTIVA) {

            try {
                construirBodyEnviarMail.construir(
                        ejecutivo.getCorreo(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        result.size()
                );
            } catch (NoResultException ex) {
                LOGGER.info("No result al enviar correo masivo", ex);
            } catch (Exception ex) {
                LOGGER.error("Error enviando correo masivo", ex);
            }
        }
    }
}