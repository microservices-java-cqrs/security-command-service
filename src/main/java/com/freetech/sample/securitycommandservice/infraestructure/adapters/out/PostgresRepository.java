package com.freetech.sample.securitycommandservice.infraestructure.adapters.out;

import com.freetech.sample.securitycommandservice.infraestructure.ports.out.EntityRepository;
import enums.StateEnum;
import interfaces.PersistenceAdapter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.util.HashMap;
import java.util.Map;

@PersistenceAdapter
public class PostgresRepository implements EntityRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public <T> T save(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public <T> T update(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public <K,T> T getById(K id, Class<T> clazz) {
        return entityManager.find(clazz, id);
    }

    @Override
    public <T> T getByField(String field, Object value, Class<T> clazz) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(field.replace(".", ""), value);
        parameters.put("logState", StateEnum.ACTIVE.getValue());

        StringBuilder sql = new StringBuilder();
        sql.append( "SELECT t FROM " )
                .append( clazz.getName() )
                .append( " t WHERE t.")
                .append( field )
                .append( " = :" )
                .append( field.replace(".","") )
                .append( " AND t.logState = :logState " );

        TypedQuery<T> query = createQuery(sql.toString(), parameters, clazz);
        var result = query.getResultList();
        return result.size() > 0 ? result.get(0) : null;
    }

    @Override
    public <T> Long countByField(String field, Object value, Class<T> clazz) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(field.replace(".", ""), value);
        parameters.put("logState", StateEnum.ACTIVE.getValue());

        StringBuilder sql = new StringBuilder();
        sql.append( "SELECT COUNT(t.id) FROM " )
                .append( clazz.getName() )
                .append( " t WHERE t.")
                .append( field )
                .append( " = :" )
                .append( field.replace(".","") )
                .append( " AND t.logState = :logState " );

        Query query = createQuery(sql.toString(), parameters);
        return (Long) query.getSingleResult();
    }

    private <X> TypedQuery<X> createQuery(String jpql, Map<String, Object> parameters, Class<X> entityClass) {
        TypedQuery<X> query = entityManager.createQuery(jpql, entityClass);
        if (parameters != null) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return query;
    }

    private Query createQuery(String jpql, Map<String, Object> parameters) {
        Query query = entityManager.createQuery(jpql);
        if(parameters != null) {
            for(Map.Entry<String, Object> entry : parameters.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return query;
    }
}
