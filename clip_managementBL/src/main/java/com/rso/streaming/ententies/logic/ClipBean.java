package com.rso.streaming.ententies.logic;

import com.kumuluz.ee.common.config.EeConfig;
import com.kumuluz.ee.common.runtime.EeRuntime;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.rso.streaming.ententies.Clip;
import org.apache.logging.log4j.ThreadContext;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RequestScoped
public class ClipBean {

    @PersistenceContext(unitName = "clipPersistanceUnit")
    private EntityManager em;

    @Inject
    private RestConfig RestConfig;

    private static final Logger LOG = LogManager.getLogger(ClipBean.class.getName());

    @PostConstruct
    private void init() {
        HashMap settings = new HashMap();
        settings.put("environment", EeConfig.getInstance().getEnv().getName());
        settings.put("serviceName", EeConfig.getInstance().getName());
        settings.put("applicationVersion", EeConfig.getInstance().getVersion());
        settings.put("uniqueInstanceId", EeRuntime.getInstance().getInstanceId());
        settings.put("uniqueRequestId", UUID.randomUUID().toString());

        ThreadContext.putAll(settings);
    }

    public List<Clip> getClips() {
        Query q = em.createNamedQuery("Clip.findAll");
        List<Clip> cl = (List<Clip>)q.getResultList();

        LOG.info("Returning all clips: {}", cl);
        return cl;
    }

    public Clip getClip(long clipId) {
        Clip c = em.find(Clip.class, clipId);
        LOG.info("Returning clip with ID: {}: {}", clipId, c);
        return c;
    }

    public Clip createClip(Clip clip) {
        LOG.info("Creating clip: {}", clip);

        if(RestConfig.getWriteEnabled()){
            try {
                beginTx();
                em.persist(clip);
                commitTx();
                LOG.info("Clip created.");
            } catch (Exception e) {
                rollbackTx();
                LOG.error("Clip creation not succesfull. {}", e);
            }

            return clip;
        } else {
            LOG.info("Cannot create clip. Write operations not enabled.");
            return null;
        }
    }

    public boolean deleteClip(long clipId) {
        LOG.info("Removing clip with ID: {}", clipId);

        if(RestConfig.getWriteEnabled()){
            Clip c = getClip(clipId);

            if(c != null){
                try {
                    beginTx();
                    em.remove(c);
                    commitTx();
                    LOG.info("Removed clip.");
                } catch (Exception e) {
                    LOG.error("Failed to remove clip: {}. An error occured: {}.", c, e);
                    rollbackTx();
                }

                return true;
            }
            LOG.info("Clip with ID: {} not found.", clipId);
        }
        LOG.info("Cannot remove clip. Write operations not enabled.");
        return false;
    }

    public Clip putClip(Clip clip) {
        LOG.info("Updating clip: {}", clip);

        if(RestConfig.getWriteEnabled()){
            Clip c = em.find(Clip.class, clip.getID());

            if (c != null) {
                try {
                    beginTx();
                    c = em.merge(clip);
                    commitTx();
                    LOG.info("Clip updated.");
                } catch (Exception e) {
                    rollbackTx();
                    LOG.error("Failed to update clip: {}. An error occured: {}.", c, e);
                }

                return c;
            }
            LOG.info("Clip with ID: {} not found.", clip.getID());
        }
        LOG.info("Cannot update clip. Write operations not enabled.");
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
