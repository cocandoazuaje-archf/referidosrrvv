package cl.cnsv.referidosrrvv.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppStarter implements ServletContextListener {

    private static final Logger LOGGER =
            LogManager.getLogger(AppStarter.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        // ⚠️ En Log4j2 ya NO se usa PropertyConfigurator
        // La configuración se hace automáticamente con:
        // log4j2.xml o log4j2.properties en el classpath

        LOGGER.info("AppStarter: contextInitialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("AppStarter: contextDestroyed");
    }
}