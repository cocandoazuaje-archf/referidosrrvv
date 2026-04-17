/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cns.integracion.wsreferidosrrvv.clases;

import cl.cns.integracion.wsreferidosrrvv.controller.RolesusuariosJpaController;
import cl.cnsv.referidosrrvv.controller.exceptions.RollbackFailureException;
import cl.cnsv.referidosrrvv.models.Rolesusuarios;
import java.math.BigInteger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.transaction.UserTransaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author cow
 */
public class ImportarRolesUsuarios {

    private static final Logger LOGGER = LogManager.getLogger(ImportarRolesUsuarios.class);

    public ImportarRolesUsuarios() {
        super();
    }

    public void cargar(
            EntidadDeCargaJs rjs,
            UserTransaction utx,
            EntityManager em) throws RollbackFailureException, Exception {
        RolesusuariosJpaController rujc = new RolesusuariosJpaController(em);

        Query q = em.createNamedQuery("Rolesusuarios.findByNombre");
        q.setParameter("nombre", rjs.getUSUARIO().toLowerCase());

        try {
            Rolesusuarios u = (Rolesusuarios) q.getSingleResult();
            u.setNombre(rjs.getUSUARIO());
            u.setRol(rjs.getROL());
            u.setAst(rjs.getAST());
            u.setSup(rjs.getSUP());
            rujc.edit(u);
        } catch (NoResultException e) {
            LOGGER.info(e);
            Rolesusuarios ou = new Rolesusuarios();
            ou.setVersion(BigInteger.ONE);
            ou.setNombre(rjs.getUSUARIO());
            ou.setRol(rjs.getROL());
            ou.setAst(rjs.getAST());
            ou.setSup(rjs.getSUP());
            rujc.create(ou);
        }
    }
}
