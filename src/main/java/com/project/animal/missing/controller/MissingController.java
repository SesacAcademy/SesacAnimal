package com.project.animal.missing.controller;

import com.project.animal.missing.constant.EndPoint;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class MissingController {
  public Map<String, String> createLinkConstants(String ...destinations) {
    Map<String, String> endPoints = Map.of(
            "edit", EndPoint.MISSING_BASE + EndPoint.EDIT,
            "delete",  EndPoint.MISSING_BASE + EndPoint.DELETE,
            "detail", EndPoint.MISSING_BASE + EndPoint.DETAIL,
            "list",  EndPoint.MISSING_BASE + EndPoint.LIST
    );

    return Arrays.stream(destinations).collect(
            Collectors.toMap((d) -> d, (d) -> endPoints.get(d)));
  }
}
