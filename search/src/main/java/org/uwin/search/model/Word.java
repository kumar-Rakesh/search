package org.uwin.search.model;

import lombok.Builder;
import lombok.Data;

import java.util.PriorityQueue;

@Data
@Builder
public class Word {

    private String word;
    private PriorityQueue<Page> pages;

}
