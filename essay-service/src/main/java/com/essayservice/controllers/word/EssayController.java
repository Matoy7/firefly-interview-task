package com.essayservice.controllers.word;

import com.essayservice.controllers.BaseResponse;
import com.essayservice.dtos.FileRequestDto;
import com.essayservice.entities.essayparsingrequest.EssayParsingRequest;
import com.essayservice.services.essayparsingrequest.EssayParsingRequestService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("words")
public class EssayController {
    private EssayParsingRequestService essayParsingRequestService;


    @Autowired
    public EssayController(EssayParsingRequestService essayParsingRequestService) {
        this.essayParsingRequestService = essayParsingRequestService;
    }

    @PostMapping
    @RequestMapping(value = "/save-essay-parsing-requests-from-file")
    public ResponseEntity<BaseResponse> saveBankWordsFromFile(@RequestBody FileRequestDto essayParsingRequestsDto) {
        if (essayParsingRequestsDto == null) {
            return new ResponseEntity("please enter valid filePath in request body", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String filePath = essayParsingRequestsDto.getFilePath();
        try {
            log.info("save words from file request: " + filePath + " had received");
            List<EssayParsingRequest> savedWords = essayParsingRequestService.saveEssayParsingRequestsFromFile(filePath);
            return new ResponseEntity(new BaseResponse("number of created words", savedWords.size()), HttpStatus.OK);
        } catch (Exception exception) {
            log.error("save words from file request" + filePath + " failed with this error: " + exception);
            return new ResponseEntity("There was an error, please contact support", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}