package com.skynet.example.postgres.repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.Validate;
import org.hibernate.query.criteria.internal.OrderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author samuel.cuellar
 * 
 * @param <T> Class of entity
 * @param <S> Class of entity key
 */
@Repository
public abstract class AbstractRepository<T, S extends Serializable> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractRepository.class);
    private static final String NOT_NULL_ENTITY_MSG = "The entity cannot be null";
    private EntityManager entityManager;
    private CriteriaBuilder criteriaBuilder;

    /**
     * Nicer way of getting a single result without a spurious exception thrown and caught.
     * 
     * @param <E>
     * @param query
     *            ready to be executed.
     * @return single result or null if none found.
     * 
     * @throws NonUniqueResultException
     *             if more than one result is found.
     */
    protected final <E> E singleResultOrNull(TypedQuery<E> query) {
        if (query.getMaxResults() > 2) {
            query.setMaxResults(2);

        }
        List<E> results = query.getResultList();
        switch (results.size()) {
        case 0 :
            return null;
        case 1 :
            return results.get(0);
        default :
            throw new NonUniqueResultException();
        }
    }

	@PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    /**
     * Finds the entity with the specified id.
     * 
     * @param id
     *            The id of the entity to search for.
     * @return Returns the entity with the specified id, or null if no such entity exists.
     */
    public T find(S id) {
        Validate.notNull(id, "The entity ID cannot be null");
        return entityManager.find(getPersistentClass(), id);
    }

    /**
     * Removes the specified entity from the database.
     * 
     * @param entity
     *            The entity to remove. It cannot be null.
     */
    public void remove(final T entity) {
        Validate.notNull(entity, NOT_NULL_ENTITY_MSG);
        entityManager.remove(entity);
    }

    /**
     * Removes the specified entity from the database.
     * 
     * @param entity
     *            The entity to remove. It cannot be null.
     */
    public void remove(S id) {
        remove(find(id));
    }

    protected abstract Class<T> getPersistentClass();

    /**
     * Saves a new entity or updates an existing entity to the database.
     * 
     * @param entity
     *            The entity to save. It cannot be null.
     */
    public void save(final T entity) {
        Validate.notNull(entity, NOT_NULL_ENTITY_MSG);
        entityManager.persist(entity);
    }

    protected final EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * @return the criteriaBuilder
     */
    protected CriteriaBuilder getCriteriaBuilder() {
        return criteriaBuilder;
    }

    /**
     * 
     * @return a list with all persisted entities
     */
    public List<T> findAll() {
        CriteriaQuery<T> cq = createCriteriaQuery();
        cq.from(getPersistentClass());
        return entityManager.createQuery(cq).getResultList();
    }

    public List<T> findAllOrderBy(String attributeName, boolean asc) {

        CriteriaQuery<T> criteriaQuery = createCriteriaQuery();
        Root<T> entity = criteriaQuery.from(getPersistentClass());
        Order order = new OrderImpl(entity.get(attributeName), asc);
        criteriaQuery.orderBy(order);
        return getEntityManager().createQuery(criteriaQuery).getResultList();
    }

    public List<T> findAllPagedSorted(int start, int limit, String attributeName, boolean asc) {
        CriteriaQuery<T> criteriaQuery = createCriteriaQuery();
        Root<T> entity = criteriaQuery.from(getPersistentClass());
        Order order = new OrderImpl(entity.get(attributeName), asc);
        criteriaQuery.orderBy(order);

        TypedQuery<T> query = getEntityManager().createQuery(criteriaQuery);
        query.setMaxResults(limit);
        query.setFirstResult(start * limit);
        return query.getResultList();
    }

    protected CriteriaQuery<T> createCriteriaQuery() {
        return criteriaBuilder.createQuery(getPersistentClass());
    }

    public T merge(final T entity) {
        Validate.notNull(entity, NOT_NULL_ENTITY_MSG);
        return entityManager.merge(entity);
    }

    public void refresh(final T entity) {
        Validate.notNull(entity, NOT_NULL_ENTITY_MSG);
        entityManager.refresh(entity);
    }

    public void flush() {
        entityManager.flush();
    }

    public List<T> findByIds(Collection<S> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        CriteriaQuery<T> criteriaQuery = createCriteriaQuery();
        Root<T> entity = criteriaQuery.from(getPersistentClass());
        criteriaQuery.where(entity.in(ids));
        return getEntityManager().createQuery(criteriaQuery).getResultList();
    }

    public void clear() {
        entityManager.clear();
    }

}
