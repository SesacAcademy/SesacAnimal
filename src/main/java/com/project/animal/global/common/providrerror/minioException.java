package com.project.animal.global.common.providrerror;

import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@NoArgsConstructor
public class minioException extends RuntimeException{
    public minioException(String message){
        super(message);
        log.info(message);
    }
}
