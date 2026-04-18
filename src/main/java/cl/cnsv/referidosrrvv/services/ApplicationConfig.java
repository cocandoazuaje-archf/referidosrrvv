/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.cnsv.referidosrrvv.services;

import java.util.Set;
import jakarta.ws.rs.core.Application;

/**
 *
 * @author cow
 */
@jakarta.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

        @Override
        public Set<Class<?>> getClasses() {
                Set<Class<?>> resources = new java.util.HashSet<>();
                addRestResourceClasses(resources);
                return resources;
        }

        /**
         * Do not modify addRestResourceClasses() method. It is automatically
         * populated with all resources defined in the project. If required, comment
         * out calling this method in getClasses().
         */
        private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(cl.cnsv.referidosrrvv.services.AccionesFacadeREST.class);
        resources.add(cl.cnsv.referidosrrvv.services.BitacorasFacadeREST.class);
        resources.add(cl.cnsv.referidosrrvv.services.EjecutivosFacadeREST.class);
        resources.add(cl.cnsv.referidosrrvv.services.EncuestaFacadeREST.class);
        resources.add(cl.cnsv.referidosrrvv.services.ReferenciasFacadeREST.class);
        resources.add(cl.cnsv.referidosrrvv.services.ReferidosFacadeREST.class);
        resources.add(cl.cnsv.referidosrrvv.services.RolesusuariosFacadeREST.class);
        resources.add(cl.cnsv.referidosrrvv.services.SucursalesFacadeREST.class);
        }
}
