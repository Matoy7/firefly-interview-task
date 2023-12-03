package com.essayservice.repositories.word;

import com.essayservice.entities.word.Word;
import com.essayservice.repositories.BaseMongoRepository;

import java.util.List;
import java.util.Map;

public interface WordMongoRepository extends BaseMongoRepository<Word> {
     void saveWords(Map<String, Integer> wordOccurrences);
    List<Word> getMostOccurringWords(int topHighestWordsNumber);
 }
