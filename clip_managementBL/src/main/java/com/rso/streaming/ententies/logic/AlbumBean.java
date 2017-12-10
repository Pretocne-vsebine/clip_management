package com.rso.streaming.ententies.logic;

import com.kumuluz.ee.logs.*;
import com.rso.streaming.ententies.Album;
import com.rso.streaming.ententies.Clip;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
public class AlbumBean {

    @PersistenceContext(unitName = "clipPersistanceUnit")
    private EntityManager em;

    @Inject
    private RestConfig RestConfig;

    private static final Logger LOG = LogManager.getLogger(AlbumBean.class.getName());

    public List<Album> getAlbums() {
        Query q = em.createNamedQuery("Album.findAll");
        List<Album> al = (List<Album>)q.getResultList();

        LOG.trace("Returning all albums: {}", al);
        return al;
    }

    public Album getAlbum(long albumId) {
        Album a = em.find(Album.class, albumId);
        LOG.trace("Returning album with ID: {}: {}", albumId, a);
        return em.find(Album.class, albumId);
    }

    @Transactional
    public Album createAlbum(Album album) {
        LOG.trace("Creating album: {}", album);

        if(RestConfig.getWriteEnabled()){
            try {
                beginTx();
                em.persist(album);
                commitTx();
                LOG.trace("Album created.");
            } catch (Exception e) {
                LOG.error("Album creation not succesfull. {}", e);
                rollbackTx();
            }
            return album;
        }
        LOG.trace("Cannot create album. Write operations not enabled.");
        return null;
    }

    public Album addClip(long albumId, Clip c) {
        LOG.trace("Adding clip: {} to album with ID: {}", c, albumId);

        if(RestConfig.getWriteEnabled()) {
            Album a = getAlbum(albumId);

            if (a != null) {
                List<Clip> clips = a.getClips();
                clips.add(c);
                a.setClips(clips);

                try {
                    beginTx();
                    a = em.merge(a);
                    commitTx();
                    LOG.trace("Added clip to album");
                } catch (Exception e) {
                    rollbackTx();
                    LOG.error("Failed to add clip: {} to album: {}. An error occured: {}.", c, a, e);
                }

                return a;
            }
            LOG.trace("Album with ID: {} not found.", albumId);
        }
        LOG.trace("Cannot add clip to album. Write operations not enabled.");
        return null;
    }

    public boolean deleteAlbum(long albumId) {
        LOG.trace("Removing album with ID: {}", albumId);

        if(RestConfig.getWriteEnabled()) {
            Album a = em.find(Album.class, albumId);

            if (a != null) {
                try {
                    beginTx();
                    em.remove(a);
                    commitTx();
                    LOG.trace("Removed album.");
                } catch (Exception e) {
                    rollbackTx();
                    LOG.error("Failed to remove album: {}. An error occured: {}.", a, e);
                }

                return true;
            }
            LOG.trace("Album with ID: {} not found.", albumId);
        }
        LOG.trace("Cannot remove album. Write operations not enabled.");
        return false;
    }

    public Album putAlbum(Album album) {
        LOG.trace("Updating album: {}", album);

        if(RestConfig.getWriteEnabled()) {
            Album a = em.find(Album.class, album.getID());

            if (a != null) {
                try {
                    beginTx();
                    a = em.merge(album);
                    commitTx();
                    LOG.trace("Album updated.");
                } catch (Exception e) {
                    rollbackTx();
                    LOG.error("Failed to update album: {}. An error occured: {}.", a, e);
                }

                return a;
            }
            LOG.trace("Album with ID: {} not found.", album.getID());
        }
        LOG.trace("Cannot update album. Write operations not enabled.");
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
