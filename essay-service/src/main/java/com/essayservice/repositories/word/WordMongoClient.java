package com.essayservice.repositories.word;

import com.essayservice.entities.word.Word;
import com.essayservice.repositories.BaseBaseMongoRepositoryClient;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class WordMongoClient extends BaseBaseMongoRepositoryClient<Word> implements WordMongoRepository {
    private MongoTemplate mongoTemplate;

    public WordMongoClient(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    protected Class getEntityClass() {
        return Word.class;
    }

    @Override
    public void saveWords(Map<String, Integer> wordOccurrences) {
        List<Pair<Query, Update>> updatesList = new ArrayList<>();
        for (Map.Entry<String, Integer> wordOccurrenceEntry : wordOccurrences.entrySet()) {
            String word = wordOccurrenceEntry.getKey();
            int wordOccurrence = wordOccurrenceEntry.getValue();
            Query query = new Query().addCriteria(new Criteria("word").is(word));
            Update update = new Update().inc("numberOfOccurrences", wordOccurrence);
            updatesList.add(Pair.of(query, update));
        }
        BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, getEntityClass());
        bulkOperations.upsert(updatesList);
        bulkOperations.execute();
    }

    @Override
    public List<Word> getMostOccurringWords(int topHighestWordsNumber) {
        SortOperation sortByPopDesc = sort(Sort.by(Sort.Direction.DESC, "numberOfOccurrences"));
        LimitOperation limitOperation = limit(topHighestWordsNumber);
        Aggregation aggregation = newAggregation(sortByPopDesc, limitOperation);
        AggregationResults<Word> result = mongoTemplate.aggregate(
                aggregation, "word", Word.class);
        return result.getMappedResults();
    }
}
