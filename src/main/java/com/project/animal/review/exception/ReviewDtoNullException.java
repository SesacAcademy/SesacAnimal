package com.project.animal.review.exception;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@NoArgsConstructor
@Log4j2
public class ReviewDtoNullException extends RuntimeException{
    public ReviewDtoNullException(String message) {
        super(message);
        log.info(message);
    }
}
