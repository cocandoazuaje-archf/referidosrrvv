/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.clases;

import cl.cnsv.referidosrrvv.controller.SucursalesJpaController1;
import cl.cnsv.referidosrrvv.controller.exceptions.RollbackFailureException;
import cl.cnsv.referidosrrvv.models.Sucursales;
import jakarta.persistence.EntityManager;
import jakarta.transaction.UserTransaction;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD
import jakarta.persistence.EntityManager;
import jakarta.transaction.UserTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
<<<<<<< HEAD
=======
import  java.util.logging.Logger;

>>>>>>> 1141929deb487e775cd53dcc1693762570b88cfc
=======
>>>>>>> d0df4d5 (Actualizacion y comentarios)

/**
 *
 * @author uvv
 */
public class CrearSucursales {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrearSucursales.class);

    public CrearSucursales() {
        // constructor vacío
    }

    public List<ErrorCrearSucursalesOut> cargar(
            List<EntidadDeCargaJs> rjsList,
            UserTransaction utx,
            EntityManager em) throws RollbackFailureException, Exception {

        SucursalesJpaController1 sc = new SucursalesJpaController1(utx, null, em);
        List<ErrorCrearSucursalesOut> ecrsList = new ArrayList<>();

        for (EntidadDeCargaJs rjs : rjsList) {
            try {
                if (rjs.getCODSUCURSAL() == null) {
                    // CREATE
                    Sucursales s = new Sucursales();
                    s.setVersion(BigInteger.ONE);
                    s.setNombre(rjs.getSUCURSAL() != null ? rjs.getSUCURSAL().trim() : null);
                    s.setDireccion(rjs.getDIRECCION());
                    s.setComuna(rjs.getCOMUNA());
                    s.setRegion(rjs.getREGION());
                    sc.create(s);

                } else {
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> d0df4d5 (Actualizacion y comentarios)
                    // UPDATE
                    Sucursales s = sc.findSucursales(rjs.getCODSUCURSAL());

                    if (s == null) {
                        throw new Exception("Sucursal no existe: " + rjs.getCODSUCURSAL());
                    }

<<<<<<< HEAD
=======
                    s = sc.findSucursales(rjs.getCODSUCURSAL());
>>>>>>> 1141929deb487e775cd53dcc1693762570b88cfc
=======
>>>>>>> d0df4d5 (Actualizacion y comentarios)
                    s.setNombre(rjs.getSUCURSAL() != null ? rjs.getSUCURSAL().trim() : null);
                    s.setDireccion(rjs.getDIRECCION());
                    s.setComuna(rjs.getCOMUNA());
                    s.setRegion(rjs.getREGION());
                    sc.edit(s);
                }

            } catch (Exception e) {
<<<<<<< HEAD
<<<<<<< HEAD
                LOGGER.error("Error procesando sucursal", e);

=======
               LOGGER.log(java.util.logging.Level.SEVERE, "Error ocurrido", e);
>>>>>>> 1141929deb487e775cd53dcc1693762570b88cfc
=======
                LOGGER.error("Error procesando sucursal", e);

>>>>>>> d0df4d5 (Actualizacion y comentarios)
                ErrorCrearSucursalesOut ecrs = new ErrorCrearSucursalesOut();
                ecrs.setCODSUCURSAL(rjs.getCODSUCURSAL());
                ecrs.setSUCURSAL(rjs.getSUCURSAL());
                ecrs.setDIRECCION(rjs.getDIRECCION());
                ecrs.setCOMUNA(rjs.getCOMUNA());
                ecrs.setREGION(rjs.getREGION());
<<<<<<< HEAD
<<<<<<< HEAD
                ecrs.setERROR("Error procesando sucursal: " + e.getMessage());

=======
                ecrs.setERROR(
                        "ecrs.setERROR("Error procesando sucursal: " + e.getMessage()); "
                                + rjs.getCODSUCURSAL()
                                + " , la sucursal no existe.");
>>>>>>> 1141929deb487e775cd53dcc1693762570b88cfc
=======
                ecrs.setERROR("Error procesando sucursal: " + e.getMessage());

>>>>>>> d0df4d5 (Actualizacion y comentarios)
                ecrsList.add(ecrs);
            }
        }
        return ecrsList;
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