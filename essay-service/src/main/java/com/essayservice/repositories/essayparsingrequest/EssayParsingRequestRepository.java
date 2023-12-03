package com.essayservice.repositories.essayparsingrequest;

import com.essayservice.entities.essayparsingrequest.EssayParsingRequest;
import com.essayservice.entities.essayparsingrequest.ParsingStatus;
import com.essayservice.repositories.BaseMongoRepository;

import java.util.List;

public interface EssayParsingRequestRepository extends BaseMongoRepository<EssayParsingRequest> {
    List<EssayParsingRequest> getAllReadyToParseRequest(int requestsLimit);
 }
