package org.uwin.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.uwin.search.config.AppConfig;
import org.uwin.search.exception.base.SearchEngineException;
import org.uwin.search.model.Page;
import org.uwin.search.model.Trie;
import org.uwin.search.model.impl.TernarySearchTree;
import org.uwin.search.util.InputStream;
import org.uwin.search.util.WordComparator;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import static org.uwin.search.model.enums.Constant.HTML;
import static org.uwin.search.model.enums.Constant.TEXT;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndexService {

    private final Trie<Long> wordMap;
    private final AppConfig config;
    private final InputStream inputStream;

    public void index() {
        String path = config.getPath();
        log.info("Path->{}", path);
        String htmlPath = path + HTML + "\\";
        log.info("Html Path->{}", htmlPath);
        String textPath = path + TEXT + "\\";
        log.info("Text Path->{}", textPath);
        File htmlFolder = new File(htmlPath);
        File textFolder = new File(textPath);
        File[] htmlFiles = htmlFolder.listFiles();
        File[] textFiles = textFolder.listFiles();
        index(htmlFiles, textFiles);
    }

    private void index(File[] htmlFiles, File[] textFiles) {
        for (int i = 0; i < textFiles.length; ++i) {
            if (textFiles[i].isFile()) {
                Trie<Long> trie = new TernarySearchTree<Long>();
                String[] tokens = scan(textFiles[i]);
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
                        long occurrences = trie.get(token);
                        if (wordMap.contains(token)) {
                            PriorityQueue<Page> pq = wordMap.getPages(token);
                            long currentOccurrences = wordMap.get(token);
                            Page page = pq.peek();
                            if (page.getOccurrences() < occurrences && pq.size() == config.getLimit()) {
                                pq.poll();
                            }
                            pq.add(Page.builder()
                                    .page(htmlFiles[i].getAbsolutePath())
                                    .occurrences(occurrences)
                                    .build());
                            wordMap.put(token, occurrences + currentOccurrences, pq);
                        } else {
                            PriorityQueue<Page> pq = new PriorityQueue<>(new WordComparator());
                            pq.add(Page.builder()
                                    .page(htmlFiles[i].getAbsolutePath())
                                    .occurrences(occurrences)
                                    .build());
                            wordMap.put(token, occurrences, pq);
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
