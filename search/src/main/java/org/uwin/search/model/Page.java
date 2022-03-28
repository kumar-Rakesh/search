package org.uwin.search.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Page {

    private String page;
    private long occurrences;
}
