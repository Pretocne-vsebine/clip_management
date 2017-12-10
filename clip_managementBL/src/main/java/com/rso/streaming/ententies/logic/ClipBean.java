package com.rso.streaming.ententies.logic;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
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
import java.util.List;

@RequestScoped
public class ClipBean {

    @PersistenceContext(unitName = "clipPersistanceUnit")
    private EntityManager em;

    @Inject
    private RestConfig RestConfig;

    private static final Logger LOG = LogManager.getLogger(ClipBean.class.getName());

    public List<Clip> getClips() {
        Query q = em.createNamedQuery("Clip.findAll");
        List<Clip> cl = (List<Clip>)q.getResultList();

        LOG.trace("Returning all clips: {}", cl);
        return cl;
    }

    public Clip getClip(long clipId) {
        Clip c = em.find(Clip.class, clipId);
        LOG.trace("Returning clip with ID: {}: {}", clipId, c);
        return c;
    }

    public Clip createClip(Clip clip) {
        LOG.trace("Creating clip: {}", clip);

        if(RestConfig.getWriteEnabled()){
            try {
                beginTx();
                em.persist(clip);
                commitTx();
                LOG.trace("Clip created.");
            } catch (Exception e) {
                rollbackTx();
                LOG.error("Clip creation not succesfull. {}", e);
            }

            return clip;
        } else {
            LOG.trace("Cannot create clip. Write operations not enabled.");
            return null;
        }
    }

    public boolean deleteClip(long clipId) {
        LOG.trace("Removing clip with ID: {}", clipId);

        if(RestConfig.getWriteEnabled()){
            Clip c = getClip(clipId);

            if(c != null){
                try {
                    beginTx();
                    em.remove(c);
                    commitTx();
                    LOG.trace("Removed clip.");
                } catch (Exception e) {
                    LOG.error("Failed to remove clip: {}. An error occured: {}.", c, e);
                    rollbackTx();
                }

                return true;
            }
            LOG.trace("Clip with ID: {} not found.", clipId);
        }
        LOG.trace("Cannot remove clip. Write operations not enabled.");
        return false;
    }

    public Clip putClip(Clip clip) {
        LOG.trace("Updating clip: {}", clip);

        if(RestConfig.getWriteEnabled()){
            Clip c = em.find(Clip.class, clip.getID());

            if (c != null) {
                try {
                    beginTx();
                    c = em.merge(clip);
                    commitTx();
                    LOG.trace("Clip updated.");
                } catch (Exception e) {
                    rollbackTx();
                    LOG.error("Failed to update clip: {}. An error occured: {}.", c, e);
                }

                return c;
            }
            LOG.trace("Clip with ID: {} not found.", clip.getID());
        }
        LOG.trace("Cannot update clip. Write operations not enabled.");
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
