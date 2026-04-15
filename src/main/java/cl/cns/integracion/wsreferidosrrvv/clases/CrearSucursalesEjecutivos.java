package cl.cns.integracion.wsreferidosrrvv.clases;

import cl.cns.integracion.wsreferidosrrvv.controller.EjecutivosJpaController;
import cl.cns.integracion.wsreferidosrrvv.controller.SucursalesJpaController;
import cl.cnsv.referidosrrvv.models.Ejecutivos;
import cl.cnsv.referidosrrvv.models.Sucursales;

import jakarta.persistence.EntityManager;
import jakarta.transaction.UserTransaction;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CrearSucursalesEjecutivos {

    private static final Logger LOGGER =
            LogManager.getLogger(CrearSucursalesEjecutivos.class);

    public void cargar(
            EntidadDeCargaJs rjs,
            UserTransaction utx,
            EntityManager em) throws Exception {

        if (rjs == null) {
            LOGGER.warn("Entidad de carga nula");
            return;
        }

        SucursalesJpaController sc = new SucursalesJpaController(em);
        EjecutivosJpaController ec = new EjecutivosJpaController(em);

        // =========================
        // SUCURSAL
        // =========================
        Sucursales sucursal = null;

        if (rjs.getSUCURSAL() != null && !rjs.getSUCURSAL().isBlank()) {

            try {
                // ✅ CORREGIDO: método correcto
                sucursal = sc.findByNombre(rjs.getSUCURSAL());
            } catch (Exception ex) {
                LOGGER.error("Error buscando sucursal", ex);
            }

            if (sucursal == null) {
                sucursal = new Sucursales();
                sucursal.setNombre(rjs.getSUCURSAL());
                sucursal.setVersion(BigInteger.ONE);
                sc.create(sucursal);
            }
        }

        // =========================
        // VALIDACIÓN EJECUTIVO
        // =========================
        if (rjs.getEJECUTIVO() == null || rjs.getEJECUTIVO().isBlank()) {
            LOGGER.warn("Registro sin ejecutivo, omitido");
            return;
        }

        // =========================
        // CÓDIGO EJECUTIVO
        // =========================
        String codigo = rjs.getCODEJECUTIVO();

        if (codigo == null || codigo.isBlank()) {
            codigo = "AUTO-" + UUID.randomUUID();
        }

        codigo = codigo.toLowerCase();

        // =========================
        // BUSCAR / UPSERT EJECUTIVO
        // =========================
        Ejecutivos ejecutivo = null;

        try {
            ejecutivo = ec.findByCodEjecutiva(codigo);
        } catch (Exception ex) {
            LOGGER.error("Error buscando ejecutivo", ex);
        }

        boolean esNuevo = (ejecutivo == null);

        if (esNuevo) {
            ejecutivo = new Ejecutivos();
            ejecutivo.setCodigo(codigo);
            ejecutivo.setVersion(BigInteger.ONE);
            ejecutivo.setReferenciasCollection(new ArrayList<>());
        }

        // =========================
        // SET DATOS
        // =========================
        ejecutivo.setNombre(rjs.getEJECUTIVO());
        ejecutivo.setCorreo(rjs.getCORREO());
        ejecutivo.setSucursalId(sucursal);

        // =========================
        // PERSISTENCIA
        // =========================
        try {
            if (esNuevo) {
                ec.create(ejecutivo);
                LOGGER.info("Ejecutivo creado: {}", codigo);
            } else {
                ec.edit(ejecutivo);
                LOGGER.info("Ejecutivo actualizado: {}", codigo);
            }
        } catch (Exception ex) {
            LOGGER.error("Error persistiendo ejecutivo", ex);
            throw ex;
        }
    }
}