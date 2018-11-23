package com.qingjiang.lettuce.utils;

/**
 * @author qingjiang.li
 * @date 2018/11/22
 */
public class StringUtil {

  private StringUtil() {}



  public static boolean isBlank(String str){
    if (str == null){
      return true;
    }
    return str.trim().isEmpty();
  }

  public static boolean notBlank(String str){
    return !isBlank(str);
  }




}
