package com.project.animal.global.common.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collectors;

public class BindingResultParser {
  public static Map<String, String> parse(BindingResult bindingResult) {

    return bindingResult
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                    FieldError::getField,
                    FieldError::getDefaultMessage
            ));
  }
}
