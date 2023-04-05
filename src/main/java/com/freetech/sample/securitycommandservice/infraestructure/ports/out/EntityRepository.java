package com.freetech.sample.securitycommandservice.infraestructure.ports.out;

public interface EntityRepository {
    <T> T save(T entity);

    <T> T update(T entity);

    <K,T> T getById(K id, Class<T> clazz);

    <T> T getByField(String field, Object value, Class<T> clazz);
    <T> Long countByField(String field, Object value, Class<T> clazz);
}
