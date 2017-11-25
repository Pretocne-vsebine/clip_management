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
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class AlbumBean {

    @PersistenceContext(unitName = "clipPersistanceUnit")
    private EntityManager em;

    @Inject
    private RestConfig RestConfig;

    public List<Album> getAlbums() {
        Query q = em.createNamedQuery("Album.findAll");
        List<Album> al = (List<Album>)q.getResultList();

        return al;
    }

    public Album getAlbum(long albumId) {
        return em.find(Album.class, albumId);
    }

    @Transactional
    public Album createAlbum(Album album) {
        if(RestConfig.getWriteEnabled()){
            try {
                beginTx();
                em.persist(album);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }

            return album;
        }
        return null;
    }

    public Album addClip(long albumId, Clip c) {
        if(RestConfig.getWriteEnabled()) {
            Album a = getAlbum(albumId);

            if (a != null) {
                List<Clip> clips = a.getClips();
                clips.add(c);

                try {
                    beginTx();
                    a = em.merge(a);
                    commitTx();
                } catch (Exception e) {
                    rollbackTx();
                }

                return a;
            }
        }
        return null;
    }

    public boolean deleteAlbum(long albumId) {
        if(RestConfig.getWriteEnabled()) {
            Album a = em.find(Album.class, albumId);

            if (a != null) {
                try {
                    beginTx();
                    em.remove(a);
                    commitTx();
                } catch (Exception e) {
                    rollbackTx();
                }

                return true;
            }
        }
        return false;
    }

    public Album putAlbum(Album album) {
        if(RestConfig.getWriteEnabled()) {
            Album a = em.find(Album.class, album.getID());

            if (a != null) {
                try {
                    beginTx();
                    a = em.merge(album);
                    commitTx();
                } catch (Exception e) {
                    rollbackTx();
                }

                return a;
            }
        }
        return null;
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
