package org.axrunner.exception;

import com.google.common.base.Strings;
import org.axrunner.api.response.ErrorResponse;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 *
 */
public class ResourceExceptionMapper implements ExceptionMapper<ResourceException> {
    @Override
    public Response toResponse(ResourceException e) {
        Response.StatusType status = e.getResponse().getStatusInfo();

        ErrorResponse entity = new ErrorResponse();

        entity.setStatus(status.getStatusCode());
        if (Strings.isNullOrEmpty(e.getMessage())) {
            entity.setMessage(status.getReasonPhrase());
        } else {
            entity.setMessage(e.getMessage());
        }

        return Response.status(status)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(entity)
                .build();
    }
}