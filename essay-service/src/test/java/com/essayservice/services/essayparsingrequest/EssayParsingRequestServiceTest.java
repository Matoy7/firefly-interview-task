package com.essayservice.services.essayparsingrequest;


import com.essayservice.entities.bankword.BankWord;
import com.essayservice.entities.essayparsingrequest.EssayParsingRequest;
import com.essayservice.entities.essayparsingrequest.ParsingStatus;
import com.essayservice.repositories.essayparsingrequest.EssayParsingRequestRepository;
import com.essayservice.services.essayparsingrequest.EssayParsingRequestService;
import com.essayservice.services.httpservice.HttpService;
import com.essayservice.services.word.WordService;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EssayParsingRequestServiceTest {
    @InjectMocks
    private EssayParsingRequestService essayParsingRequestService;
    @Mock
    private EssayParsingRequestRepository essayParsingRequestRepository;
    @Mock
    private WordService wordService;
    @Mock
    private HttpService httpService;

    @Test
    public void handleParseRequest_urlWithContent_theWordOccurrencesCorrectlyStoredInDB() throws IOException {
        //prep

        initWordBank();
        EssayParsingRequest essayParsingRequest = new EssayParsingRequest(new ObjectId().toHexString(), "http://www.somurl.com", ParsingStatus.READY_FOR_PARSE, new Date(), new HashMap<>());
        String urlContent = " hello. word1 word2 word1 word1 word1 word2 ";
        Mockito.when(httpService.getHtmlContentFromUrl(eq("http://www.somurl.com"))).thenReturn(urlContent);

        // method call
        essayParsingRequestService.handleParseRequest(essayParsingRequest);

        // assert
        ArgumentCaptor<Map<String, Integer>> wordOccurrencesCaptor = ArgumentCaptor.forClass(Map.class);
        verify(wordService, times(1)).saveWords(wordOccurrencesCaptor.capture());
        Map<String, Integer> wordOccurrences = wordOccurrencesCaptor.getValue();

        Assert.assertNotNull(wordOccurrences);
        Assert.assertEquals("4", wordOccurrences.get("word1").toString());
        Assert.assertEquals("2", wordOccurrences.get("word2").toString());

        ArgumentCaptor<EssayParsingRequest> essayParsingRequestCaptor = ArgumentCaptor.forClass(EssayParsingRequest.class);
        verify(essayParsingRequestRepository, times(1)).save(essayParsingRequestCaptor.capture());
        EssayParsingRequest essayParsingRequestReturned = essayParsingRequestCaptor.getValue();

        Assert.assertNotNull(essayParsingRequestReturned);
        Assert.assertEquals(ParsingStatus.FINISHED, essayParsingRequestReturned.getParsingStatus());

    }

    @Test
    public void handleParseRequest_urlWithContentCheckCaseSensitivity_theWordOccurrencesCorrectlyStoredInDB() throws IOException {
        //prep

        initWordBank();
        EssayParsingRequest essayParsingRequest = new EssayParsingRequest(new ObjectId().toHexString(), "http://www.somurl.com", ParsingStatus.READY_FOR_PARSE, new Date(), new HashMap<>());
        String urlContent = " hello. word1 word2 WORD1 wOrD1 Word1 woRd2 ";
        Mockito.when(httpService.getHtmlContentFromUrl(eq("http://www.somurl.com"))).thenReturn(urlContent);

        // method call
        essayParsingRequestService.handleParseRequest(essayParsingRequest);

        // assert
        ArgumentCaptor<Map<String, Integer>> wordOccurrencesCaptor = ArgumentCaptor.forClass(Map.class);
        verify(wordService, times(1)).saveWords(wordOccurrencesCaptor.capture());
        Map<String, Integer> wordOccurrences = wordOccurrencesCaptor.getValue();

        Assert.assertNotNull(wordOccurrences);
        Assert.assertEquals("4", wordOccurrences.get("word1").toString());
        Assert.assertEquals("2", wordOccurrences.get("word2").toString());

        ArgumentCaptor<EssayParsingRequest> essayParsingRequestCaptor = ArgumentCaptor.forClass(EssayParsingRequest.class);
        verify(essayParsingRequestRepository, times(1)).save(essayParsingRequestCaptor.capture());
        EssayParsingRequest essayParsingRequestReturned = essayParsingRequestCaptor.getValue();

        Assert.assertNotNull(essayParsingRequestReturned);
        Assert.assertEquals(ParsingStatus.FINISHED, essayParsingRequestReturned.getParsingStatus());

    }

    @Test
    public void handleParseRequest_urlWithEmptyContent_noWordsWereStoredInDB() throws IOException {
        //prep

        initWordBank();
        EssayParsingRequest essayParsingRequest = new EssayParsingRequest(new ObjectId().toHexString(), "http://www.somurl.com", ParsingStatus.READY_FOR_PARSE, new Date(), new HashMap<>());
        String urlContent = "";
        Mockito.when(httpService.getHtmlContentFromUrl(eq("http://www.somurl.com"))).thenReturn(urlContent);

        // method call
        essayParsingRequestService.handleParseRequest(essayParsingRequest);

        // assert
        ArgumentCaptor<Map<String, Integer>> wordOccurrencesCaptor = ArgumentCaptor.forClass(Map.class);
        verify(wordService, times(1)).saveWords(wordOccurrencesCaptor.capture());
        Map<String, Integer> wordOccurrences = wordOccurrencesCaptor.getValue();

        Assert.assertNotNull(wordOccurrences);
        Assert.assertEquals(0, wordOccurrences.size());

        ArgumentCaptor<EssayParsingRequest> essayParsingRequestCaptor = ArgumentCaptor.forClass(EssayParsingRequest.class);
        verify(essayParsingRequestRepository, times(1)).save(essayParsingRequestCaptor.capture());
        EssayParsingRequest essayParsingRequestReturned = essayParsingRequestCaptor.getValue();

        Assert.assertNotNull(essayParsingRequestReturned);
        Assert.assertEquals(ParsingStatus.FINISHED, essayParsingRequestReturned.getParsingStatus());

    }

    @Test
    public void handleParseRequest_urlWitNullContent_noWordsWereStoredInDB() throws IOException {
        //prep

        initWordBank();
        EssayParsingRequest essayParsingRequest = new EssayParsingRequest(new ObjectId().toHexString(), "http://www.somurl.com", ParsingStatus.READY_FOR_PARSE, new Date(), new HashMap<>());
        String urlContent = null;
        Mockito.when(httpService.getHtmlContentFromUrl(any())).thenReturn(urlContent);

        // method call
        essayParsingRequestService.handleParseRequest(essayParsingRequest);

        // assert
        ArgumentCaptor<Map<String, Integer>> wordOccurrencesCaptor = ArgumentCaptor.forClass(Map.class);
        verify(wordService, times(1)).saveWords(wordOccurrencesCaptor.capture());
        Map<String, Integer> wordOccurrences = wordOccurrencesCaptor.getValue();

        Assert.assertNotNull(wordOccurrences);
        Assert.assertEquals(0, wordOccurrences.size());

        ArgumentCaptor<EssayParsingRequest> essayParsingRequestCaptor = ArgumentCaptor.forClass(EssayParsingRequest.class);
        verify(essayParsingRequestRepository, times(1)).save(essayParsingRequestCaptor.capture());
        EssayParsingRequest essayParsingRequestReturned = essayParsingRequestCaptor.getValue();

        Assert.assertNotNull(essayParsingRequestReturned);
        Assert.assertEquals(ParsingStatus.FINISHED, essayParsingRequestReturned.getParsingStatus());

    }

    private void initWordBank() {
        List<BankWord> bankWords = new ArrayList();
        bankWords.add(new BankWord(null, "word1"));
        bankWords.add(new BankWord(null, "word2"));
        bankWords.add(new BankWord(null, "word3"));
        essayParsingRequestService.setBankWords(bankWords);
    }

}
