package com.project.animal.global.common.utils;

import java.time.LocalDateTime;

public class CustomDateParser {
  private static final String DEFAULT_TIME = "T00:00:00";

  public static LocalDateTime formatDate(String date) {
    if (date == null || date.isEmpty() || date.isBlank()) return null;
    return LocalDateTime.parse(date + DEFAULT_TIME);
  }
}
