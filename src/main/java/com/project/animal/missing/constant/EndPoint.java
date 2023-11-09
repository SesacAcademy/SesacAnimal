package com.project.animal.missing.constant;

abstract public class EndPoint {
  public static final String ID = "postId";
  public static final String PATH_ID = "/{" + ID + "}";
  public static final String MISSING = "/missing";
  public static final String LIST = "/list";
  public static final String DETAIL = PATH_ID;
  public static final String EDIT = PATH_ID;
  public static final String DELETE = PATH_ID;
  public static final String CREATE = "";
  public static final String NEW = "/new";

}
