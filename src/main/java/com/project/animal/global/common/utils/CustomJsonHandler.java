package com.project.animal.global.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class CustomJsonHandler {

  private final ObjectMapper objectMapper;

  public <T> String stringify(T value) {
    try {
      String jsonValue = objectMapper.writeValueAsString(value);
      return jsonValue;
    } catch (JsonProcessingException e) {
      log.error("Json 생성 중 문제 발생: >> " + value);
      throw new RuntimeException("Json 생성 중 문제가 발생하였습니다.", e);
    }
  }

}
