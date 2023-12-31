package com.project.animal.missing.constant;

abstract public class EndPoint {

  // To do : 해당 EndPoint 클래스 제거
  public static final String ID_KEY = "postId";
  public static final String VERSION = "/v1";
  public static final String MISSING = "/missing";
  public static final String PATH_ID = "/{" + ID_KEY + "}";
  public static final String MISSING_BASE = VERSION + MISSING;
  public static final String LIST = "/list";
  public static final String EDIT = "/edit";
  public static final String NEW = "/new";
  public static final String DETAIL = "/detail";
  public static final String DELETE = "/delete";
  public static final String CREATE = "";

  public static final String COMMENT= "/comment";


}
