package com.essayservice.services.httpservice;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class HttpService {
    public String getHtmlContentFromUrl(String url) throws IOException {
        Document httpDocument = Jsoup.connect(url).get();
        String urlContent = Jsoup.parse(httpDocument.html()).wholeText();
        return urlContent;
    }

}
