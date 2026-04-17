package cl.cns.integracion.wsreferidosrrvv.util;

import cl.cns.integracion.wsreferidosrrvv.vo.ReferidosRRVV_Out;
import java.util.Arrays;
import java.util.List;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WebApplicationExceptionMapper
        implements ExceptionMapper<WebApplicationException> {

    /**
     * Logger de la clase.
     */
    private static final Logger LOGGER = LogManager.getLogger(WebApplicationExceptionMapper.class);

    @Override
    public Response toResponse(WebApplicationException exception) {
        return Response
                .status(getCode(exception))
                .entity(prepareMessage(exception))
                .type("application/json")
                .build();
    }

    private ReferidosRRVV_Out prepareMessage(WebApplicationException exception) {
        LOGGER.error(
                "Se capturo una excepcion en la llamada al servicio",
                exception);
        ReferidosRRVV_Out out = new ReferidosRRVV_Out();
        out.setCodigo(1);
        out.setMensaje(exception.getMessage());
        return out;
    }

    private int getCode(WebApplicationException wex) {
        if (wex.getResponse() != null) {
            return wex.getResponse().getStatus();
        } else {
            return 500;
        }
    }
}
