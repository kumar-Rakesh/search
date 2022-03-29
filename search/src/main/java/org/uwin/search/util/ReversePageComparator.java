package org.uwin.search.util;

import org.uwin.search.model.Page;

import java.util.Comparator;

public class ReversePageComparator implements Comparator<Page> {

    public int compare(Page p1, Page p2) {
        return p2.getOccurrences() < p1.getOccurrences() ? -1 : 1;
    }
}
