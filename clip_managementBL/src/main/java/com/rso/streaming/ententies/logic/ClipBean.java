package com.rso.streaming.ententies.logic;

import com.rso.streaming.ententies.Clip;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@ApplicationScoped
public class ClipBean {

    @PersistenceContext(unitName = "clipPersistanceUnit")
    private EntityManager em;

    @Inject
    private RestConfig RestConfig;

    public List<Clip> getClips() {
        Query q = em.createNamedQuery("Clip.findAll");
        return (List<Clip>)q.getResultList();
    }

    public Clip getClip(long clipId) {
        return em.find(Clip.class, clipId);
    }

    public Clip createClip(Clip clip) {
        if(RestConfig.getWriteEnabled()){
            try {
                beginTx();
                em.persist(clip);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }

            return clip;
        } else
            return null;
    }

    public boolean deleteClip(long clipId) {
        if(RestConfig.getWriteEnabled()){
            Clip c = getClip(clipId);

            if(c != null){
                try {
                    beginTx();
                    em.remove(c);
                    commitTx();
                } catch (Exception e) {
                    rollbackTx();
                }

                return true;
            }
        }
        return false;
    }

    public Clip putClip(Clip clip) {
        if(RestConfig.getWriteEnabled()){
            Clip c = em.find(Clip.class, clip.getID());

            if (c != null) {
                try {
                    beginTx();
                    c = em.merge(clip);
                    commitTx();
                } catch (Exception e) {
                    rollbackTx();
                }

                return c;
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
