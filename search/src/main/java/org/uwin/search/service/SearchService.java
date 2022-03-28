package org.uwin.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.uwin.search.model.Page;
import org.uwin.search.model.Trie;
import org.uwin.search.util.ReverseWordComparator;

import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final Trie<Long> wordMap;

    public List<Page> search(String word) {
        if (wordMap.contains(word)) {
            PriorityQueue<Page> pq = wordMap.getPages(word);
            return pq.stream().sorted(new ReverseWordComparator()).collect(Collectors.toList());
        }
        return List.of();
    }
}
