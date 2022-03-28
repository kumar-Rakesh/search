package org.uwin.search.exception;

import lombok.NoArgsConstructor;
import org.uwin.search.exception.base.SearchEngineException;

@NoArgsConstructor
public class IllegalArgumentException extends SearchEngineException {

    public IllegalArgumentException(String message) {
        super(message);
    }

}
