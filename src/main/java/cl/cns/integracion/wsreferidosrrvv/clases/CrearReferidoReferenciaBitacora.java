package cl.cns.integracion.wsreferidosrrvv.clases;

import cl.cns.integracion.wsreferidosrrvv.controller.AccionesJpaController;
import cl.cns.integracion.wsreferidosrrvv.controller.BitacorasJpaController;
import cl.cns.integracion.wsreferidosrrvv.controller.ReferenciasJpaController;
import cl.cnsv.referidosrrvv.clases.CrearReferidoReferenciaBitacora.ErrorCrearReferidoReferenciaBitacoraOut;
import cl.cnsv.referidosrrvv.models.Referidos;
import jakarta.persistence.EntityManager;
import jakarta.transaction.UserTransaction;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CrearReferidoReferenciaBitacora {

    private String noSePudoInsertar = "No se pudo insertar el referido -> ";

    private static final Logger LOGGER =
            LogManager.getLogger(CrearReferidoReferenciaBitacora.class);

    public List<ErrorCrearReferidoReferenciaBitacoraOut> cargar(
            List<EntidadDeCargaJs> rjsList,
            UserTransaction utx,
            EntityManager em) {

        List<ErrorCrearReferidoReferenciaBitacoraOut> outList = new ArrayList<>();

        // Controllers reutilizados (IMPORTANTE)
        ReferenciasJpaController refc = new ReferenciasJpaController(em);
        AccionesJpaController ac = new AccionesJpaController(em);
        BitacorasJpaController bc = new BitacorasJpaController(em);

        for (EntidadDeCargaJs rjs : rjsList) {

            try {
                String vRut = generarRut(rjs);

                Object[] valores = continuar2(rjs, utx, em, vRut);

                OutVerificaReferenciaActiva ovr =
                        (OutVerificaReferenciaActiva) valores[0];

                Referidos r = (Referidos) valores[1];
                Referidos referidoExiste = (Referidos) valores[2];

                if (ovr.isTieneReferenciaActiva()) {
                    outList.add(error(rjs, "Ya posee una referencia activa"));
                    continue;
                }

                Referidos finalR = continuar8(rjs, r, referidoExiste, utx, em, vRut);

                // 🔴 AQUÍ debes tener implementado continuar4 en esta clase o llamarlo correctamente
                continuar4(rjs, refc, ac, finalR, bc);

            } catch (Exception e) {
                LOGGER.error("Error carga", e);
                outList.add(error(rjs, e.getMessage()));
            }
        }

        return outList;
    }

    private Object[] continuar2(
            EntidadDeCargaJs rjs,
            UserTransaction utx,
            EntityManager em,
            String vRut) {

        VerificarReferenciaActivaReferido vrar = new VerificarReferenciaActivaReferido();
        OutVerificaReferenciaActiva ovr = vrar.verificar(vRut, utx, em);

        Referidos referidoExiste = ovr.getReferido();

        if (referidoExiste != null && referidoExiste.getId() == null) {
            referidoExiste = null;
        }

        Referidos r = new Referidos();

        return new Object[]{ovr, r, referidoExiste};
    }

    private String generarRut(EntidadDeCargaJs rjs) {
        String vRut = "SR" + String.valueOf(new Date().getTime()).substring(5, 13);
        if (rjs.getRUT() != null && rjs.getRUT().length() > 1) {
            vRut = rjs.getRUT();
        }
        return vRut.toUpperCase();
    }

    private ErrorCrearReferidoReferenciaBitacoraOut error(
            EntidadDeCargaJs rjs, String msg) {

        ErrorCrearReferidoReferenciaBitacoraOut e =
                new ErrorCrearReferidoReferenciaBitacoraOut();

        e.setRUT(rjs.getRUT());
        e.setNOMBRE(rjs.getNOMBRE());
        e.setAPELLIDOS(rjs.getAPELLIDOS());
        e.setERROR(noSePudoInsertar + msg);

        return e;
    }

    // ⚠️ ESTO ES CRÍTICO: aquí debes tener TU lógica real
    private Referidos continuar8(
            EntidadDeCargaJs rjs,
            Referidos r,
            Referidos referidoExiste,
            UserTransaction utx,
            EntityManager em,
            String vRut) {

        // placeholder seguro
        r.setRut(vRut);
        r.setNombre(rjs.getNOMBRE());

        return r;
    }

    // ⚠️ también debes implementar esto o traerlo de tu versión original
    private void continuar4(
            EntidadDeCargaJs rjs,
            ReferenciasJpaController refc,
            AccionesJpaController ac,
            Referidos r,
            BitacorasJpaController bc) {

        // implementación real depende de tu lógica existente
    }
}