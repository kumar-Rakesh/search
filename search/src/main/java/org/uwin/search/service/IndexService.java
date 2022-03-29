package org.uwin.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.uwin.search.config.AppConfig;
import org.uwin.search.exception.base.SearchEngineException;
import org.uwin.search.model.Page;
import org.uwin.search.model.Trie;
import org.uwin.search.model.WebPage;
import org.uwin.search.model.impl.TernarySearchTree;
import org.uwin.search.util.InputStream;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndexService {

    private final Trie<Long> wordMap;
    private final AppConfig config;
    private final InputStream inputStream;

    public void index(WebPage webPage) {
        List<File> htmlFiles = webPage.getHtmlFiles();
        List<File> textFiles = webPage.getTextFiles();
        index(htmlFiles, textFiles);
    }

    private void index(List<File> htmlFiles, List<File> textFiles) {
        for (int i = 0; i < textFiles.size(); ++i) {
            File textFile = textFiles.get(i);
            String htmlFilePath = htmlFiles.get(i).getAbsolutePath();
            if (textFile.isFile()) {
                Trie<Long> trie = new TernarySearchTree<Long>();
                String[] tokens = scan(textFile);
                Set<String> tokensSet = new HashSet<>();
                for (String token : tokens) {
                    if (trie.contains(token)) {
                        long count = trie.get(token);
                        trie.put(token, count + 1);
                    } else {
                        trie.put(token, 1L);
                    }
                    tokensSet.add(token);
                }
                for (String token : tokensSet) {
                    if (trie.contains(token)) {
                        log.info("Token: {}", token);
                        long newOccurrences = trie.get(token);
                        TreeMap<Page, Page> pages = (TreeMap<Page, Page>) wordMap.getAllPages(token);
                        if (wordMap.contains(token)) {
                            long currentTotalOccurrences = wordMap.get(token);
                            if (wordMap.containsPage(token, htmlFilePath)) {
                                Page page = wordMap.getPage(token, htmlFilePath);
                                long currentPageOccurrences = page.getOccurrences();
                                if (currentPageOccurrences != newOccurrences) {
                                    wordMap.removePage(token, htmlFilePath);
                                    page.setOccurrences(newOccurrences);
                                    wordMap.put(token, currentTotalOccurrences - currentPageOccurrences + newOccurrences, page);
                                }
                            } else {
                                if (pages.size() == config.getLimit()) {
                                    Page p = pages.remove(pages.firstKey());
                                    currentTotalOccurrences -= p.getOccurrences();
                                }
                                wordMap.put(token, currentTotalOccurrences + newOccurrences, Page.builder().page(htmlFilePath).occurrences(newOccurrences).build());
                            }
                        } else {
                            wordMap.put(token, newOccurrences, Page.builder().page(htmlFilePath).occurrences(newOccurrences).build());
                        }
                    }
                }
            }
        }
    }

    private String[] scan(File file) {
        try {
            String[] tokens = inputStream.read(file);
            return tokens;
        } catch (IOException ex) {
            throw new SearchEngineException();
        }
    }

}
