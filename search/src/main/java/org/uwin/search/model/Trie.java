package org.uwin.search.model;

import java.util.PriorityQueue;

public interface Trie<V> extends Tree {

    V get(String key);

    V put(String key, V value);

    V put(String key, V value, PriorityQueue<Page> pages);

    PriorityQueue<Page> getPages(String key);
}
