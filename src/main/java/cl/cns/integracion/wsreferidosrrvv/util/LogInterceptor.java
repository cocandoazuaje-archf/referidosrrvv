package cl.cns.integracion.wsreferidosrrvv.util;

import java.util.Arrays;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogInterceptor {

    private static final Logger LOGGER = LogManager.getLogger(LogInterceptor.class);

    @AroundInvoke
    public Object log(InvocationContext ctx) throws Exception {
        LOGGER.info(Constantes.INICIO);
        LOGGER.info(
                "ENTRANDO : "
                        + ctx.getMethod().getName()
                        + ",  Params"
                        + Arrays.toString(ctx.getParameters()));
        long start = System.currentTimeMillis();
        Object returnMe = ctx.proceed();
        long executionTime = System.currentTimeMillis() - start;
        LOGGER.info(
                "SALIENDO : "
                        + ctx.getMethod().getName()
                        + ":"
                        + executionTime
                        + "ms"
                        + " respuesta:"
                        + returnMe);
        LOGGER.info(Constantes.TERMINO);
        return returnMe;
    }
}
