package org.uwin.search.model.impl;

import org.uwin.search.exception.IllegalArgumentException;
import org.uwin.search.model.Page;
import org.uwin.search.model.Trie;

import java.util.Objects;
import java.util.PriorityQueue;

public class TernarySearchTree<V> implements Trie<V> {

    private int size;
    private Node root;

    private class Node {
        private char c;
        private Node left, mid, right;
        private V val;
        private PriorityQueue<Page> pages;
    }

    public int size() {
        return this.size;
    }

    public Boolean contains(String key) {
        return Objects.nonNull(get(this.root, key, 0));
    }

    public V get(String key) {
        Node node = get(this.root, key, 0);
        return Objects.isNull(node) ? null : node.val;
    }

    private Node get(Node root, String key, int index) {
        if (Objects.isNull(root)) {
            return null;
        }
        if (Objects.isNull(key) || key.isEmpty()) {
            throw new IllegalArgumentException("Key can't be null or empty!");
        }
        char expectedChar = key.charAt(index);
        if (expectedChar < root.c) {
            return get(root.left, key, index);
        } else if (expectedChar > root.c) {
            return get(root.right, key, index);
        } else if (index < key.length() - 1) {
            return get(root.mid, key, index + 1);
        } else {
            return Objects.isNull(root.val) ? null : root;
        }
    }

    public V put(String key, V value) {
        if (Objects.isNull(key) || key.isEmpty()) {
            throw new IllegalArgumentException("Key can't be null or empty!");
        }
        return put(key, value, null);
    }

    public V put(String key, V value, PriorityQueue<Page> pages) {
        if (Objects.isNull(get(this.root, key, 0))) {
            ++this.size;
        }
        return (this.root = put(this.root, key, value, pages, 0)).val;
    }

    private Node put(Node root, String key, V value, PriorityQueue<Page> pages, int index) {
        char c = key.charAt(index);
        if (Objects.isNull(root)) {
            root = new Node();
            root.c = c;
        }
        if (c < root.c) {
            root.left = put(root.left, key, value, pages, index);
        } else if (c > root.c) {
            root.right = put(root.right, key, value, pages, index);
        } else if (index < key.length() - 1) {
            root.mid = put(root.mid, key, value, pages, index + 1);
        } else {
            root.val = value;
            root.pages = pages;
        }
        return root;
    }

    public PriorityQueue<Page> getPages(String key) {
        return getPages(root, key);
    }

    private PriorityQueue<Page> getPages(Node root, String key) {
        Node node = get(root, key, 0);
        if (Objects.nonNull(node)) {
            return node.pages;
        }
        return null;
    }

}
