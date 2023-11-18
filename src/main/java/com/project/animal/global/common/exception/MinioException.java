package com.project.animal.global.common.exception;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@NoArgsConstructor
public class MinioException extends RuntimeException{
    public MinioException(String message){
        super(message);
        log.info(message);
    }
}
