package de.berlin.htw.entity.dao;

import de.berlin.htw.entity.dto.UserEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Alexander Stanik [stanik@htw-berlin.de]
 */
@ApplicationScoped
public class UserRepository {

    @PersistenceContext
    EntityManager entityManager;
    
    public UserEntity findUserById(final Integer id) {
        return entityManager.find(UserEntity.class, id);
    }

    public List<UserEntity> getAllUsers() {
        TypedQuery<UserEntity> query = entityManager.createQuery("SELECT u FROM UserEntity u", UserEntity.class);
        return query.getResultList();
    }
    
    @Transactional
    public Integer persistUser(@Valid final UserEntity user) {
        entityManager.persist(user);
        return user.getId();
    }

    @Transactional
    public UserEntity updateUser(@Valid final UserEntity user) {
        return entityManager.merge(user);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public EntityManager getEntityManager() {
        return entityManager;
    }
}