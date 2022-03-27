package org.uwin.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.uwin.search.config.AppConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.uwin.search.util.RegexPattern.SPECIAL_CHAR;
import static org.uwin.search.util.RegexPattern.URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlerService {

    private final AppConfig config;
    private static final String HTML_EXT = ".html";
    private static final String TXT_EXT = ".txt";

    public List<String> crawl(String url) throws IOException {
        List<String> urls = getUrls(url);
        htmlToText(urls);
        return urls;
    }

    private List<String> getUrls(String url) throws IOException {
        List<String> urls = new ArrayList<>();
        Document document = Jsoup.connect(url).get();
        Elements links = document.select("a[href]");
        for (Element link : links) {
            String s = link.attr("abs:href");
            String regex = URL;
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(s);
            while (m.find()) {
                urls.add(m.group(0));
            }
        }
        return urls;
    }

    private void htmlToText(List<String> urls) throws IOException {
        for (String url : urls) {
            String regex = SPECIAL_CHAR;
            String name = url.replaceAll(regex, "");
            Document document = Jsoup.connect(url).get();
            String outputPath = config.getPath();
            String html = document.html();
            String text = document.text();
            writeToFile(outputPath + "html\\", name, html, HTML_EXT);
            writeToFile(outputPath + "text\\", name, text, TXT_EXT);
        }
    }

    private void writeToFile(String folderPath, String fileName, String text, String ext) throws IOException {
        BufferedWriter writer = null;
        try {
            File outputFolder = new File(folderPath);
            if (!outputFolder.exists() && !outputFolder.isDirectory()) {
                outputFolder.mkdir();
            }
            writer = new BufferedWriter(new FileWriter(folderPath + fileName + ext));
            writer.write(text);
            writer.close();
        } finally {
            if (Objects.nonNull(writer)) {
                writer.close();
            }
        }
    }
}
