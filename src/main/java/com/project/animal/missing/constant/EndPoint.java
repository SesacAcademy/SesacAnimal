package com.project.animal.missing.constant;

abstract public class EndPoint {

  public static final String ID_KEY = "postId";
  public static final String VERSION = "/v1";
  public static final String MISSING = "/missing";
  public static final String PATH_ID = "/{" + ID_KEY + "}";

  public static final String MISSING_BASE = VERSION + MISSING;
  public static final String LIST = "/list";
  public static final String EDIT = "/edit";
  public static final String NEW = "/new";
  public static final String DETAIL = "";
  public static final String DELETE = "";
  public static final String CREATE = "";


}
