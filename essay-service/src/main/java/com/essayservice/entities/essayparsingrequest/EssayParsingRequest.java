package com.essayservice.entities.essayparsingrequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Document("essayParsingRequest")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EssayParsingRequest {
    @Id
    private Object id;
    private String url;
    private ParsingStatus parsingStatus;
    private Date creationDate;
    private Map<String, Integer> wordsOccurrences;

}
