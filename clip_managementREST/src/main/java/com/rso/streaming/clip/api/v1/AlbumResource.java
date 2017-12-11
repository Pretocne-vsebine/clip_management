package com.rso.streaming.clip.api.v1;

import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.logs.cdi.LogParams;
import com.rso.streaming.clip.api.v1.logger.LogContextInterceptor;
import com.rso.streaming.ententies.Album;
import com.rso.streaming.ententies.logic.AlbumBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Api(value = "Albums")
@RequestScoped
@Path("/albums")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log(LogParams.METRICS)
public class AlbumResource {

    @Inject
    private AlbumBean albumBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    @ApiOperation(value = "Get Albums", notes = "Returns a list of all albums.", response = Album.class)
    @Timed(name = "AlbumsGetTime")
    public Response getAlbums() {
        List<Album> albums = albumBean.getAlbums();

        if(albums == null || albums.isEmpty())
            return Response.status(Response.Status.NOT_FOUND).build();
        else
            return Response.ok(albums).build();
    }

    @GET
    @ApiOperation(value = "Get an album", response = Album.class)
    @Path("/{albumId}")
    public Response getAlbum(@PathParam("albumId") long albumId) {

        Album album = albumBean.getAlbum(albumId);

        if (album == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(album).build();
    }

    @GET
    @Path("/healthResponseCheck")
    public Response getCheck() {
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Metered(name = "AlbumCreation")
    public Response createAlbum(Album album) {

        if (album.getTitle().isEmpty() || album.getArtist().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            album = albumBean.createAlbum(album);
        }

        if (album != null && album.getID() != null) {
            return Response.status(Response.Status.CREATED).entity(album).build();
        } else {
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @PUT
    public Response putAlbum(Album album) {
        album = albumBean.putAlbum(album);

        if (album == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            if (album.getID() != null)
                return Response.status(Response.Status.OK).entity(album).build();
            else
                return Response.status(Response.Status.NOT_MODIFIED).build();
        }
    }

    @DELETE
    @Path("{albumId}")
    @Metered(name = "AlbumDeletion")
    public Response deleteAlbum(@PathParam("albumId") Long albumId) {
        boolean deleted = albumBean.deleteAlbum(albumId);

        if (deleted) {
            return Response.status(Response.Status.GONE).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
