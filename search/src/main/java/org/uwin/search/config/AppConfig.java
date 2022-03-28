package org.uwin.search.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.uwin.search.model.Page;
import org.uwin.search.model.Trie;
import org.uwin.search.model.impl.TernarySearchTree;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

@Data
@Configuration
public class AppConfig {

    @Value("${file-path}")
    private String path;

    @Value("${size}")
    private int limit;

    @Bean
    public Trie<Long> getTernarySearchTree() {
        return new TernarySearchTree<Long>();
    }

    @Bean
    public Map<String, PriorityQueue<Page>> getWordMap() {
        return new TreeMap<String, PriorityQueue<Page>>();
    }
}
