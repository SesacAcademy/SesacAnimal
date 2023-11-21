package com.project.animal.missing.controller.utils;

import com.project.animal.missing.constant.EndPoint;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PathMaker {
  public Map<String, String> createLink(String ...destinations) {
    Map<String, String> endPoints = Map.of(
            "new", EndPoint.MISSING_BASE + EndPoint.NEW,
            "edit", EndPoint.MISSING_BASE + EndPoint.EDIT,
            "delete",  EndPoint.MISSING_BASE + EndPoint.DELETE,
            "detail", EndPoint.MISSING_BASE + EndPoint.DETAIL,
            "list",  EndPoint.MISSING_BASE + EndPoint.LIST,
            "newComment", EndPoint.MISSING_BASE + EndPoint.DETAIL + EndPoint.COMMENT + EndPoint.NEW,
            "editComment", EndPoint.MISSING_BASE + EndPoint.DETAIL + EndPoint.COMMENT + EndPoint.EDIT,
            "deleteComment", EndPoint.MISSING_BASE + EndPoint.DETAIL + EndPoint.COMMENT + EndPoint.DELETE
    );

    return Arrays.stream(destinations).collect(
            Collectors.toMap((d) -> d, (d) -> endPoints.get(d)));
  }
}
