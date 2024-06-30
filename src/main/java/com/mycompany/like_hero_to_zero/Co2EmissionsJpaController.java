/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.like_hero_to_zero;

import com.mycompany.like_hero_to_zero.exceptions.NonexistentEntityException;
import com.mycompany.like_hero_to_zero.exceptions.RollbackFailureException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.UserTransaction;
import java.util.List;

/**
 *
 * @author Kwanou Harlain
 */
public class Co2EmissionsJpaController implements Serializable {

    public Co2EmissionsJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Co2Emissions co2Emissions) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            RegisterUser userId = co2Emissions.getUserId();
            if (userId != null) {
                userId = em.getReference(userId.getClass(), userId.getId());
                co2Emissions.setUserId(userId);
            }
            em.persist(co2Emissions);
            if (userId != null) {
                userId.getCo2EmissionsCollection().add(co2Emissions);
                userId = em.merge(userId);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Co2Emissions co2Emissions) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Co2Emissions persistentCo2Emissions = em.find(Co2Emissions.class, co2Emissions.getId());
            RegisterUser userIdOld = persistentCo2Emissions.getUserId();
            RegisterUser userIdNew = co2Emissions.getUserId();
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getId());
                co2Emissions.setUserId(userIdNew);
            }
            co2Emissions = em.merge(co2Emissions);
            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getCo2EmissionsCollection().remove(co2Emissions);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getCo2EmissionsCollection().add(co2Emissions);
                userIdNew = em.merge(userIdNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = co2Emissions.getId();
                if (findCo2Emissions(id) == null) {
                    throw new NonexistentEntityException("The co2Emissions with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Co2Emissions co2Emissions;
            try {
                co2Emissions = em.getReference(Co2Emissions.class, id);
                co2Emissions.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The co2Emissions with id " + id + " no longer exists.", enfe);
            }
            RegisterUser userId = co2Emissions.getUserId();
            if (userId != null) {
                userId.getCo2EmissionsCollection().remove(co2Emissions);
                userId = em.merge(userId);
            }
            em.remove(co2Emissions);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Co2Emissions> findCo2EmissionsEntities() {
        return findCo2EmissionsEntities(true, -1, -1);
    }

    public List<Co2Emissions> findCo2EmissionsEntities(int maxResults, int firstResult) {
        return findCo2EmissionsEntities(false, maxResults, firstResult);
    }

    private List<Co2Emissions> findCo2EmissionsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Co2Emissions.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Co2Emissions findCo2Emissions(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Co2Emissions.class, id);
        } finally {
            em.close();
        }
    }

    public int getCo2EmissionsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Co2Emissions> rt = cq.from(Co2Emissions.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
