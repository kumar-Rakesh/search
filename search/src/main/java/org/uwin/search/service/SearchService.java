package org.uwin.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.uwin.search.model.Page;
import org.uwin.search.model.Trie;
import org.uwin.search.model.Word;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final Trie<Long> wordMap;

    public List<Page> search(String key) {
        if (wordMap.contains(key)) {
            Map<Page, Page> pages = wordMap.getAllPages(key);
            LinkedList<Page> searchResult = new LinkedList<>();
            pages.forEach((k, v) -> searchResult.addFirst(k));
            return searchResult;
        }
        return List.of();
    }

    public List<Word> autoComplete(String key) {
        Map<Word, Word> possibleKeys = wordMap.autoComplete(key);
        LinkedList<Word> results = new LinkedList<>();
        possibleKeys.forEach((k, v) -> results.addFirst(k));
        return results;
    }
}
