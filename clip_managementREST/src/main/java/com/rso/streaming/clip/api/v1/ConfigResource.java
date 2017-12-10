package com.rso.streaming.clip.api.v1;

import com.rso.streaming.ententies.logic.RestConfig;
import io.swagger.annotations.Api;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Api(value = "Config")
@ApplicationScoped
@Path("/config")
/* For testing purposes */
public class ConfigResource {
    @Inject
    private RestConfig restConfig;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getConfig() {
        return Response.ok(restConfig.getWriteEnabled()).build();
    }
}
