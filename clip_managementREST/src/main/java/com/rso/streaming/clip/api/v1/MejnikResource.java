package com.rso.streaming.clip.api.v1;

import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.logs.cdi.LogParams;
import com.rso.streaming.ententies.Album;
import com.rso.streaming.ententies.logic.AlbumBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@RequestScoped
@Path("/mejnik")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MejnikResource {

    @GET
    public Response getInfo() {
        String info="{\"clani\": [\"zk0928\"],\n" +
                "    \"opis_projekta\": \"Pretočne vsebine. Streaming glasbe, funkcionalnosti se niso implementirane. Website: http://35.189.118.15/\",\n" +
                "    \"mikrostoritve\": [\"http://35.189.119.116:8081/v1/albums\"],\n" +
                "    \"github\": [\"https://github.com/Pretocne-vsebine/clip_management\", \"https://github.com/Pretocne-vsebine/user_interface\"],\n" +
                "    \"travis\": [\"https://travis-ci.org/Pretocne-vsebine/clip_management\", \"https://travis-ci.org/Pretocne-vsebine/user_interface\"],\n" +
                "    \"dockerhub\": [\"https://hub.docker.com/r/zigakern/user_interface/\", \"https://hub.docker.com/r/zigakern/clip_management/\"]}";

        return Response.ok(info).build();
    }
}
