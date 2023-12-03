package com.essayservice.repositories;

import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

@Getter
public abstract class BaseBaseMongoRepositoryClient<T> implements BaseMongoRepository<T> {
    private MongoTemplate mongoTemplate;

    public BaseBaseMongoRepositoryClient(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void save(T entity) {
        mongoTemplate.save(entity);
    }

    @Override
    public void saveAll(List<T> entities) {
        BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, getEntityClass());
        bulkOperations.insert(entities);
        bulkOperations.execute();
    }


    @Override
    public List<T> getAll() {
        return mongoTemplate.findAll(getEntityClass());
    }

    @Override
    public T getById(ObjectId objectId) {
        return (T) mongoTemplate.findById(objectId, getEntityClass());

    }

    protected abstract Class getEntityClass();
}
