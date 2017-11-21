package com.rso.streaming.ententies.logic;

import com.rso.streaming.ententies.Album;
import com.rso.streaming.ententies.Clip;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
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

    public List<Album> getAlbums() {
        Query q = em.createNamedQuery("Album.findAll");
        List<Album> al = (List<Album>)q.getResultList();

        return al;
    }

    public Album getAlbum(long albumId) {
        return em.find(Album.class, albumId);
    }

    public Album createAlbum(Album album) {

        try {
            beginTx();
            em.persist(album);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return album;
    }

    public Album addClip(long albumId, Clip c) {
        Album a = getAlbum(albumId);

        if (a == null) {
            return null;
        }

        try {
            beginTx();
            List<Clip> clips = a.getClips();
            clips.add(c);
            a = em.merge(a);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return a;
    }

    public boolean deleteAlbum(long albumId) {
        Album a = em.find(Album.class, albumId);

        if (a != null) {
            try {
                beginTx();
                em.remove(a);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else
            return false;

        return true;
    }

    public Album putAlbum(Album album) {
        Album a = em.find(Album.class, album.getID());

        if (a == null) {
            return null;
        }

        try {
            beginTx();
            album = em.merge(album);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return album;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }
}
