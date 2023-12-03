package com.essayservice.repositories;

import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

public interface BaseMongoRepository<T> {
    void save(T entity);

    void saveAll(List<T> entity);

    List<T> getAll();

    T getById(ObjectId objectId);

}