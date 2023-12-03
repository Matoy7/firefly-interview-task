package com.essayservice.controllers.word;

import com.essayservice.controllers.BaseResponse;
import com.essayservice.dtos.FileRequestDto;
import com.essayservice.dtos.WordDTO;
import com.essayservice.entities.bankword.BankWord;
import com.essayservice.entities.word.Word;
import com.essayservice.services.word.WordService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("words")
public class WordController {
    private WordService wordService;


    @Autowired
    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @PostMapping
    @RequestMapping(value = "/save-bank-words-from-file")
    public ResponseEntity<BaseResponse> saveBankWordsFromFile(@RequestBody FileRequestDto saveWordsFromFileDto) {
        if (saveWordsFromFileDto == null) {
            return new ResponseEntity("please enter valid filePath in request body", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String filePath = saveWordsFromFileDto.getFilePath();
        try {
            log.info("save words from file request: " + filePath + " had received");
            List<BankWord> savedWords = wordService.saveBankWordsFromFile(filePath);
            return new ResponseEntity(new BaseResponse("number of created words", savedWords.size()), HttpStatus.OK);
        } catch (Exception exception) {
            log.error("save words from file request" + filePath + " failed with this error: " + exception);
            return new ResponseEntity("There was an error, please contact support", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping
    @RequestMapping(value = "/most-occurring-words")
    public ResponseEntity<BaseResponse> getMostOccurringWords(@RequestParam("top-highest-occurring-words-number") String topHighestOccurringWordsNumber) {
        try {
            log.info("get most occurring words request: " + topHighestOccurringWordsNumber + " had received");
            List<WordDTO> mostOccurringWords = wordService.getMostOccurringWords(topHighestOccurringWordsNumber);
            return new ResponseEntity(new BaseResponse("top " + topHighestOccurringWordsNumber + " most occurring words", mostOccurringWords), HttpStatus.OK);
        } catch (Exception exception) {
            log.error("get most occurring words request failed with this error: " + exception);
            return new ResponseEntity("There was an error, please contact support", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}