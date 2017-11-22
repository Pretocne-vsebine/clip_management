package com.rso.streaming.ententies.logic;

import com.rso.streaming.ententies.Album;
import com.rso.streaming.ententies.Clip;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Stateless
public class AlbumBean {

    @PersistenceContext(unitName = "clipPersistanceUnit")
    private EntityManager em;

    @Inject
    private RestConfig RestConfig;

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Album> getAlbums() {
        Query q = em.createNamedQuery("Album.findAll");
        List<Album> al = (List<Album>)q.getResultList();

        return al;
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Album getAlbum(long albumId) {
        return em.find(Album.class, albumId);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Album createAlbum(Album album) {
        if(RestConfig.isWriteEnabled()){
            em.persist(album);
            return album;
        }

        return null;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Album addClip(long albumId, Clip c) {
        if(RestConfig.isWriteEnabled()) {
            Album a = getAlbum(albumId);

            if (a != null) {
                List<Clip> clips = a.getClips();
                clips.add(c);
                return em.merge(a);
            }
        }
        return null;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean deleteAlbum(long albumId) {
        if(RestConfig.isWriteEnabled()) {
            Album a = em.find(Album.class, albumId);

            if (a != null) {
                em.remove(a);
                return true;
            }
        }
        return false;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Album putAlbum(Album album) {
        if(RestConfig.isWriteEnabled()) {
            Album a = em.find(Album.class, album.getID());

            if (a != null) {
                return em.merge(album);
            }
        }
        return null;
    }
}
