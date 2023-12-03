package com.essayservice.services.word;

import com.essayservice.dtos.WordDTO;
import com.essayservice.entities.bankword.BankWord;
import com.essayservice.entities.word.Word;
import com.essayservice.repositories.BaseMongoRepository;
import com.essayservice.repositories.word.WordMongoRepository;
import com.essayservice.services.BaseMapperService;
import com.utils.ScannerUtils;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Data
public class WordService extends BaseMapperService<Word, WordDTO> {

    public static final int LEGAL_CHARS_IN_WORD = 3;
    public static final String REGEX_FOR_ALPHEBIET_STRING = "^[a-zA-Z]*$";
    private final BaseMongoRepository<BankWord> wordsBankMongoRepository;
    private final WordMongoRepository wordMongoRepository;

    @Autowired
    public WordService(BaseMongoRepository<BankWord> wordsBankMongoRepository, WordMongoRepository wordMongoRepository) {
        this.wordsBankMongoRepository = wordsBankMongoRepository;
        this.wordMongoRepository = wordMongoRepository;
    }


    public List<BankWord> saveBankWordsFromFile(String bankWordsFilePath) {
        Set<String> wordsFromUrl = ScannerUtils.getLists(bankWordsFilePath);
        List<BankWord> legalWords = wordsFromUrl.stream().filter(word -> legalWord(word)).map(currentWord -> new BankWord(new ObjectId(), currentWord)).collect(Collectors.toList());
        wordsBankMongoRepository.saveAll(legalWords);
        return legalWords;
    }


    public void saveWords(Map<String, Integer> wordOccurrences) {
        if (!wordOccurrences.isEmpty()) {
            wordMongoRepository.saveWords(wordOccurrences);
        }
    }

    public List<BankWord> getAllBankWords() {
        return wordsBankMongoRepository.getAll();
    }


    public List<WordDTO> getMostOccurringWords(String topHighestWordsNumber) {
        int topHighestWordsNumberAsInteger = Integer.parseInt(topHighestWordsNumber);
        List<Word> wordList = wordMongoRepository.getMostOccurringWords(topHighestWordsNumberAsInteger);
        return wordList.stream().map(word -> toDTO(word)).collect(Collectors.toList());
    }

    private boolean legalWord(String word) {
        return word != null && word.length() >= LEGAL_CHARS_IN_WORD && word.matches(REGEX_FOR_ALPHEBIET_STRING);
    }




    @Override
    public Class getBaseEntityType() {
        return Word.class;
    }

    @Override
    public Class getDTOEntityType() {
        return WordDTO.class;
    }
}