package com.essayservice.repositories.essayparsingrequest;

import com.essayservice.entities.essayparsingrequest.EssayParsingRequest;
import com.essayservice.entities.essayparsingrequest.ParsingStatus;
import com.essayservice.repositories.BaseBaseMongoRepositoryClient;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EssayParsingRequestMongoClient extends BaseBaseMongoRepositoryClient<EssayParsingRequest> implements EssayParsingRequestRepository {

    public EssayParsingRequestMongoClient(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }

    @Override
    protected Class getEntityClass() {
        return EssayParsingRequest.class;
    }


    @Override
    public List<EssayParsingRequest> getAllReadyToParseRequest(int requestsLimit) {
        Query query = new Query(Criteria.where("ParsingStatus").is("READY_FOR_PARSE"))
                .with(new Sort(Sort.Direction.ASC, "creationDate"))
                .limit(requestsLimit);
        List<EssayParsingRequest> essayParsingRequestList = getMongoTemplate().find(query, EssayParsingRequest.class);
        updateRequestsStatus(essayParsingRequestList, ParsingStatus.IN_PROGRESS);
        return essayParsingRequestList;
    }


    private void updateRequestsStatus(List<EssayParsingRequest> essayParsingRequestList, ParsingStatus parsingStatus) {
        for (EssayParsingRequest essayParsingRequest : essayParsingRequestList) {
            essayParsingRequest.setParsingStatus(parsingStatus);
            save(essayParsingRequest);
        }
    }


}
