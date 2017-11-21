package com.rso.streaming.ententies.logic;

import com.rso.streaming.ententies.Album;
import com.rso.streaming.ententies.Clip;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class ClipBean {

    @PersistenceContext(unitName = "clipPersistanceUnit")
    private EntityManager em;

    public List<Clip> getClips() {
        Query q = em.createNamedQuery("Clip.findAll");
        return (List<Clip>)q.getResultList();
    }

    public Clip getClip(long clipId) {
        return em.find(Clip.class, clipId);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Clip createClip(Clip clip) {
        em.persist(clip);

        return clip;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean deleteClip(long clipId) {
        Clip c = getClip(clipId);

        if(c == null)
            return false;

        em.remove(c);
        return true;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Clip putClip(Clip clip) {
        Clip a = em.find(Clip.class, clip.getID());

        if (a == null) {
            return null;
        }

        clip = em.merge(clip);

        return clip;
    }
}
