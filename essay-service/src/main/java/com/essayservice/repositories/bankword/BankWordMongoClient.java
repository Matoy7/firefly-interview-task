package com.essayservice.repositories.bankword;

import com.essayservice.entities.bankword.BankWord;
import com.essayservice.repositories.BaseBaseMongoRepositoryClient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BankWordMongoClient extends BaseBaseMongoRepositoryClient<BankWord> {

    public BankWordMongoClient(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }

    @Override
    protected Class getEntityClass() {
        return BankWord.class;
    }
}
