package cl.cns.integracion.wsreferidosrrvv.services;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import jakarta.ws.rs.core.SecurityContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;

public class ServiceUtils {

    private static final Logger LOGGER = LogManager.getLogger(ServiceUtils.class);

    public static <T> List<T> findBy(
            EntityManager em,
            String nombreColumna,
            String valorBuscado,
            Class<T> clase) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(clase);
        Root<T> c = q.from(clase);
        CriteriaQuery<T> q2 = q.select(c)
                .where(cb.equal(c.get(nombreColumna), valorBuscado));
        TypedQuery<T> tquery = em.createQuery(q2);
        return tquery.getResultList();
    }

    public static <T> List<T> findAll(EntityManager em, Class<T> entityClass) {
        jakarta.persistence.criteria.CriteriaQuery cq = em
                .getCriteriaBuilder()
                .createQuery();
        cq.select(cq.from(entityClass));
        return em.createQuery(cq).getResultList();
    }

    public static String geUsername(SecurityContext sc) {
        String userName = null;
        try {
            // se usa el id como usuario
            userName = sc.getUserPrincipal().getName();
            // si esta loguado con un token de keycloack se usa el login name
            if (sc.getUserPrincipal() instanceof KeycloakPrincipal) {
                KeycloakPrincipal<KeycloakSecurityContext> kp = (KeycloakPrincipal<KeycloakSecurityContext>) sc
                        .getUserPrincipal();
                userName = kp.getKeycloakSecurityContext().getToken().getPreferredUsername();
            }
        } catch (Exception e) {
            userName = "ANONIMO";
            LOGGER.error("No se pudo obtener el usuario loguado desde el token.", e);
        }
        return userName;
    }
}
