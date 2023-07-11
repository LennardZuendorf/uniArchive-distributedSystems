package de.berlin.htw.entity.dao;

import de.berlin.htw.entity.dto.PurchaseEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@ApplicationScoped
public class PurchaseRepository {
    @PersistenceContext
    EntityManager entityManager;

    public PurchaseEntity findOrderbyId(final Integer id) {
        return entityManager.find(PurchaseEntity.class, id);
    }

    public List<PurchaseEntity> getAllOrders() {
        TypedQuery<PurchaseEntity> query = entityManager.createQuery("SELECT o FROM PurchaseEntity o", PurchaseEntity.class);
        return query.getResultList();
    }

    public List<PurchaseEntity> findOrdersByUserId(Integer userId) {
        TypedQuery<PurchaseEntity> query = entityManager.createQuery("SELECT o FROM PurchaseEntity o WHERE o.userId = :userId", PurchaseEntity.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Transactional
    public Integer persistOrder(@Valid final PurchaseEntity order) {
        entityManager.persist(order);
        return order.getId();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public EntityManager getEntityManager() {
        return entityManager;
    }

}

