package com.rso.streaming.clip.api.v1;

import com.rso.streaming.ententies.Clip;
import com.rso.streaming.ententies.logic.ClipBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Api(value = "Clips")
@RequestScoped
@Path("/clips")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClipResource {

    @Inject
    private ClipBean clipBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    @ApiOperation(value = "Get clips", response = Clip.class)
    public Response getClips() {
        List<Clip> clips = clipBean.getClips();

        if(clips == null || clips.isEmpty())
            return Response.status(Response.Status.NOT_FOUND).build();
        else
            return Response.ok(clips).build();
    }

    @GET
    @ApiOperation(value = "Get a clip", response = Clip.class)
    @Path("/{clipId}")
    public Response getClip(@PathParam("clipId") long clipId) {

        Clip c = clipBean.getClip(clipId);

        if (c == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(c).build();
    }

    @POST
    public Response createClip(Clip clip) {

        if (clip.getTitle().isEmpty() || clip.getAuthor().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            clip = clipBean.createClip(clip);
        }

        if (clip.getID() != null) {
            return Response.status(Response.Status.CREATED).entity(clip).build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity(clip).build();
        }
    }

    @PUT
    public Response putClip(Clip clip) {
        clip = clipBean.putClip(clip);

        if (clip == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            if (clip.getID() != null)
                return Response.status(Response.Status.OK).entity(clip).build();
            else
                return Response.status(Response.Status.NOT_MODIFIED).build();
        }
    }

    @DELETE
    @Path("{clipId}")
    public Response deleteClip(@PathParam("clipId") Long clipId) {
        boolean deleted = clipBean.deleteClip(clipId);

        if (deleted) {
            return Response.status(Response.Status.GONE).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
