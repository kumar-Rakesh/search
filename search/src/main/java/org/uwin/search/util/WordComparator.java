package org.uwin.search.util;

import org.uwin.search.model.Word;

import java.util.Comparator;

public class WordComparator implements Comparator<Word> {

    public int compare(Word w1, Word w2) {
        return w1.getVal() < w2.getVal() ? -1 : 1;
    }
}
