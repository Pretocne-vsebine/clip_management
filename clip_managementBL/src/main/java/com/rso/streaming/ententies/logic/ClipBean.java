package com.rso.streaming.ententies.logic;

import com.rso.streaming.ententies.Album;
import com.rso.streaming.ententies.Clip;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class ClipBean {

    @PersistenceContext(unitName = "clipPersistanceUnit")
    private EntityManager em;

    @Inject
    private RestConfig RestConfig;

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Clip> getClips() {
        Query q = em.createNamedQuery("Clip.findAll");
        return (List<Clip>)q.getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Clip getClip(long clipId) {
        return em.find(Clip.class, clipId);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Clip createClip(Clip clip) {
        if(RestConfig.isWriteEnabled()){
            em.persist(clip);

            return clip;
        } else
            return null;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean deleteClip(long clipId) {
        if(RestConfig.isWriteEnabled()){
            Clip c = getClip(clipId);

            if(c != null){
                em.remove(c);
                return true;
            }
        }
        return false;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Clip putClip(Clip clip) {
        if(RestConfig.isWriteEnabled()){
            Clip a = em.find(Clip.class, clip.getID());

            if (a != null) {
                return em.merge(clip);
            }
        }
        return null;
    }
}
