package cl.cnsv.referidosrrvv.clases;

import cl.cnsv.referidosrrvv.controller.SucursalesJpaController1;
import cl.cnsv.referidosrrvv.controller.exceptions.RollbackFailureException;
import cl.cnsv.referidosrrvv.models.Sucursales;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.transaction.UserTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CrearSucursales2 {

    private static final Logger LOGGER = LogManager.getLogger(CrearSucursales2.class);

    public CrearSucursales2() {
    }

    public List<ErrorCrearSucursalesOut> cargar(
            List<EntidadDeCargaJs> rjsList,
            UserTransaction utx,
            EntityManager em) throws RollbackFailureException, Exception {

        SucursalesJpaController1 sc = new SucursalesJpaController1(utx, null, em);
        List<ErrorCrearSucursalesOut> errores = new ArrayList<>();

        for (EntidadDeCargaJs rjs : rjsList) {

            Sucursales s = null;

            try {
<<<<<<< HEAD
                // Buscar si existe
                if (rjs.getCODSUCURSAL() != null) {
                    s = sc.findSucursales(rjs.getCODSUCURSAL());
=======
                if (s == null && rjs.getCODSUCURSAL() != null) {
                    ErrorCrearSucursalesOut ecrs = new ErrorCrearSucursalesOut();
                    ecrs.setCODSUCURSAL(rjs.getCODSUCURSAL());
                    ecrs.setSUCURSAL(rjs.getSUCURSAL());
                    ecrs.setDIRECCION(rjs.getDIRECCION());
                    ecrs.setCOMUNA(rjs.getCOMUNA());
                    ecrs.setREGION(rjs.getREGION());
                    ecrs.setERROR(
                            "ecrs.setERROR("Error procesando sucursal: " + e.getMessage()); "
                                    + rjs.getCODSUCURSAL()
                                    + " , la sucursal no existe.");
                    ecrsList.add(ecrs);
                    continue;
>>>>>>> 1141929deb487e775cd53dcc1693762570b88cfc
                }

                if (s == null) {
                    // CREAR
                    s = new Sucursales();
                    s.setVersion(BigInteger.ONE);
                    s.setNombre(rjs.getSUCURSAL() != null ? rjs.getSUCURSAL().trim() : null);
                    s.setDireccion(rjs.getDIRECCION());
                    s.setComuna(rjs.getCOMUNA());
                    s.setRegion(rjs.getREGION());

                    sc.create(s);

                } else {
                    // EDITAR
                    s.setNombre(rjs.getSUCURSAL() != null ? rjs.getSUCURSAL().trim() : null);
                    s.setDireccion(rjs.getDIRECCION());
                    s.setComuna(rjs.getCOMUNA());
                    s.setRegion(rjs.getREGION());

                    sc.edit(s);
                }

            } catch (Exception e) {
                LOGGER.error("Error procesando sucursal: {}", rjs.getCODSUCURSAL(), e);

                ErrorCrearSucursalesOut error = new ErrorCrearSucursalesOut();
                error.setCODSUCURSAL(rjs.getCODSUCURSAL());
                error.setSUCURSAL(rjs.getSUCURSAL());
                error.setDIRECCION(rjs.getDIRECCION());
                error.setCOMUNA(rjs.getCOMUNA());
                error.setREGION(rjs.getREGION());
                error.setERROR("Error procesando sucursal: " + e.getMessage());

                errores.add(error);
            }
        }

        return errores;
    }

    public static class ErrorCrearSucursalesOut {

        private BigDecimal CODSUCURSAL;
        private String SUCURSAL;
        private String NOMBRE;
        private String DIRECCION;
        private String COMUNA;
        private String REGION;
        private String ERROR;

        public BigDecimal getCODSUCURSAL() {
            return CODSUCURSAL;
        }

        public void setCODSUCURSAL(BigDecimal CODSUCURSAL) {
            this.CODSUCURSAL = CODSUCURSAL;
        }

        public String getSUCURSAL() {
            return SUCURSAL;
        }

        public void setSUCURSAL(String SUCURSAL) {
            this.SUCURSAL = SUCURSAL;
        }

        public String getNOMBRE() {
            return NOMBRE;
        }

        public void setNOMBRE(String NOMBRE) {
            this.NOMBRE = NOMBRE;
        }

        public String getDIRECCION() {
            return DIRECCION;
        }

        public void setDIRECCION(String DIRECCION) {
            this.DIRECCION = DIRECCION;
        }

        public String getCOMUNA() {
            return COMUNA;
        }

        public void setCOMUNA(String COMUNA) {
            this.COMUNA = COMUNA;
        }

        public String getREGION() {
            return REGION;
        }

        public void setREGION(String REGION) {
            this.REGION = REGION;
        }

        public String getERROR() {
            return ERROR;
        }

        public void setERROR(String ERROR) {
            this.ERROR = ERROR;
        }
    }
}