/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.like_hero_to_zero;

import com.mycompany.like_hero_to_zero.exceptions.NonexistentEntityException;
import com.mycompany.like_hero_to_zero.exceptions.PreexistingEntityException;
import com.mycompany.like_hero_to_zero.exceptions.RollbackFailureException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.UserTransaction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Kwanou Harlain
 */
public class RegisterUserJpaController implements Serializable {

    public RegisterUserJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RegisterUser registerUser) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (registerUser.getCo2EmissionsCollection() == null) {
            registerUser.setCo2EmissionsCollection(new ArrayList<Co2Emissions>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Co2Emissions> attachedCo2EmissionsCollection = new ArrayList<Co2Emissions>();
            for (Co2Emissions co2EmissionsCollectionCo2EmissionsToAttach : registerUser.getCo2EmissionsCollection()) {
                co2EmissionsCollectionCo2EmissionsToAttach = em.getReference(co2EmissionsCollectionCo2EmissionsToAttach.getClass(), co2EmissionsCollectionCo2EmissionsToAttach.getId());
                attachedCo2EmissionsCollection.add(co2EmissionsCollectionCo2EmissionsToAttach);
            }
            registerUser.setCo2EmissionsCollection(attachedCo2EmissionsCollection);
            em.persist(registerUser);
            for (Co2Emissions co2EmissionsCollectionCo2Emissions : registerUser.getCo2EmissionsCollection()) {
                RegisterUser oldUserIdOfCo2EmissionsCollectionCo2Emissions = co2EmissionsCollectionCo2Emissions.getUserId();
                co2EmissionsCollectionCo2Emissions.setUserId(registerUser);
                co2EmissionsCollectionCo2Emissions = em.merge(co2EmissionsCollectionCo2Emissions);
                if (oldUserIdOfCo2EmissionsCollectionCo2Emissions != null) {
                    oldUserIdOfCo2EmissionsCollectionCo2Emissions.getCo2EmissionsCollection().remove(co2EmissionsCollectionCo2Emissions);
                    oldUserIdOfCo2EmissionsCollectionCo2Emissions = em.merge(oldUserIdOfCo2EmissionsCollectionCo2Emissions);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findRegisterUser(registerUser.getId()) != null) {
                throw new PreexistingEntityException("RegisterUser " + registerUser + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RegisterUser registerUser) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            RegisterUser persistentRegisterUser = em.find(RegisterUser.class, registerUser.getId());
            Collection<Co2Emissions> co2EmissionsCollectionOld = persistentRegisterUser.getCo2EmissionsCollection();
            Collection<Co2Emissions> co2EmissionsCollectionNew = registerUser.getCo2EmissionsCollection();
            Collection<Co2Emissions> attachedCo2EmissionsCollectionNew = new ArrayList<Co2Emissions>();
            for (Co2Emissions co2EmissionsCollectionNewCo2EmissionsToAttach : co2EmissionsCollectionNew) {
                co2EmissionsCollectionNewCo2EmissionsToAttach = em.getReference(co2EmissionsCollectionNewCo2EmissionsToAttach.getClass(), co2EmissionsCollectionNewCo2EmissionsToAttach.getId());
                attachedCo2EmissionsCollectionNew.add(co2EmissionsCollectionNewCo2EmissionsToAttach);
            }
            co2EmissionsCollectionNew = attachedCo2EmissionsCollectionNew;
            registerUser.setCo2EmissionsCollection(co2EmissionsCollectionNew);
            registerUser = em.merge(registerUser);
            for (Co2Emissions co2EmissionsCollectionOldCo2Emissions : co2EmissionsCollectionOld) {
                if (!co2EmissionsCollectionNew.contains(co2EmissionsCollectionOldCo2Emissions)) {
                    co2EmissionsCollectionOldCo2Emissions.setUserId(null);
                    co2EmissionsCollectionOldCo2Emissions = em.merge(co2EmissionsCollectionOldCo2Emissions);
                }
            }
            for (Co2Emissions co2EmissionsCollectionNewCo2Emissions : co2EmissionsCollectionNew) {
                if (!co2EmissionsCollectionOld.contains(co2EmissionsCollectionNewCo2Emissions)) {
                    RegisterUser oldUserIdOfCo2EmissionsCollectionNewCo2Emissions = co2EmissionsCollectionNewCo2Emissions.getUserId();
                    co2EmissionsCollectionNewCo2Emissions.setUserId(registerUser);
                    co2EmissionsCollectionNewCo2Emissions = em.merge(co2EmissionsCollectionNewCo2Emissions);
                    if (oldUserIdOfCo2EmissionsCollectionNewCo2Emissions != null && !oldUserIdOfCo2EmissionsCollectionNewCo2Emissions.equals(registerUser)) {
                        oldUserIdOfCo2EmissionsCollectionNewCo2Emissions.getCo2EmissionsCollection().remove(co2EmissionsCollectionNewCo2Emissions);
                        oldUserIdOfCo2EmissionsCollectionNewCo2Emissions = em.merge(oldUserIdOfCo2EmissionsCollectionNewCo2Emissions);
                    }
                }
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
                Integer id = registerUser.getId();
                if (findRegisterUser(id) == null) {
                    throw new NonexistentEntityException("The registerUser with id " + id + " no longer exists.");
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
            RegisterUser registerUser;
            try {
                registerUser = em.getReference(RegisterUser.class, id);
                registerUser.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The registerUser with id " + id + " no longer exists.", enfe);
            }
            Collection<Co2Emissions> co2EmissionsCollection = registerUser.getCo2EmissionsCollection();
            for (Co2Emissions co2EmissionsCollectionCo2Emissions : co2EmissionsCollection) {
                co2EmissionsCollectionCo2Emissions.setUserId(null);
                co2EmissionsCollectionCo2Emissions = em.merge(co2EmissionsCollectionCo2Emissions);
            }
            em.remove(registerUser);
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

    public List<RegisterUser> findRegisterUserEntities() {
        return findRegisterUserEntities(true, -1, -1);
    }

    public List<RegisterUser> findRegisterUserEntities(int maxResults, int firstResult) {
        return findRegisterUserEntities(false, maxResults, firstResult);
    }

    private List<RegisterUser> findRegisterUserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RegisterUser.class));
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

    public RegisterUser findRegisterUser(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RegisterUser.class, id);
        } finally {
            em.close();
        }
    }

    public int getRegisterUserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RegisterUser> rt = cq.from(RegisterUser.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
