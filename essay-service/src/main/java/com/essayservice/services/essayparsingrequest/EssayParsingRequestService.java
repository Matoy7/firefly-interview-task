package com.essayservice.services.essayparsingrequest;

import com.essayservice.entities.bankword.BankWord;
import com.essayservice.entities.essayparsingrequest.EssayParsingRequest;
import com.essayservice.entities.essayparsingrequest.ParsingStatus;
import com.essayservice.repositories.essayparsingrequest.EssayParsingRequestRepository;
import com.essayservice.services.httpservice.HttpService;
import com.essayservice.services.word.WordService;
import com.utils.ScannerUtils;
import lombok.Data;
import org.bson.types.ObjectId;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Data
public class EssayParsingRequestService {

    public static final String WHITE_SPACES_REGEX = "\\s+";
    private final EssayParsingRequestRepository essayParsingRequestRepository;
    private final WordService wordService;
    private final HttpService httpService;
    private List<BankWord> bankWords;

    @Autowired
    public EssayParsingRequestService(EssayParsingRequestRepository essayParsingRequestRepository, WordService wordService, HttpService httpService) {
        this.essayParsingRequestRepository = essayParsingRequestRepository;
        this.wordService = wordService;
        this.httpService = httpService;
        this.bankWords = wordService.getAllBankWords();
    }

    public List<EssayParsingRequest> saveEssayParsingRequestsFromFile(String filePath) {
        Set<String> wordsFromFile = ScannerUtils.getLists(filePath);
        List<EssayParsingRequest> essayParsingRequests = wordsFromFile.stream().map(line -> new EssayParsingRequest(new ObjectId(), line, ParsingStatus.READY_FOR_PARSE, new Date(), null)).collect(Collectors.toList());
        essayParsingRequestRepository.saveAll(essayParsingRequests);
        return essayParsingRequests;
    }

    public List<EssayParsingRequest> getReadyParseRequests(int parseRequestLimit) {
        return essayParsingRequestRepository.getAllReadyToParseRequest(parseRequestLimit);
    }

    public void handleParseRequest(EssayParsingRequest essayParsingRequest) {
        try {
            String urlContent = httpService.getHtmlContentFromUrl(essayParsingRequest.getUrl());
            String[] wordsInEssay = urlContent != null ? urlContent.split(WHITE_SPACES_REGEX) : new String[]{};
            Map<String, Integer> wordOccurrences = getWordOccurrencesMap(wordsInEssay);
            wordService.saveWords(wordOccurrences);
            essayParsingRequest.setWordsOccurrences(wordOccurrences);
            essayParsingRequest.setParsingStatus(ParsingStatus.FINISHED);
            essayParsingRequestRepository.save(essayParsingRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Map<String, Integer> getWordOccurrencesMap(String[] wordsInEssay) {
        Map<String, Integer> wordOccurrences = new HashMap<>();
        for (int i = 0; i < wordsInEssay.length; i++) {
            String currentWord = wordsInEssay[i].toLowerCase();
            if (bankWords.contains(new BankWord(null, currentWord))) {
                int currentWordOccurrences = wordOccurrences.getOrDefault(currentWord, 0);
                currentWordOccurrences++;
                wordOccurrences.put(currentWord, currentWordOccurrences);
            }
        }
        return wordOccurrences;
    }
}