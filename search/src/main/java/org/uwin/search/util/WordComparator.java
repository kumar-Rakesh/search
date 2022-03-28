package org.uwin.search.util;

import org.uwin.search.model.Page;

import java.util.Comparator;

public class WordComparator implements Comparator<Page> {

    public int compare(Page p1, Page p2) {
        return p1.getOccurrences() < p2.getOccurrences() ? -1 : 1;
    }
}
