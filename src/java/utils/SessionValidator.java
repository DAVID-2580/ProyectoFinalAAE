/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author Angel Ricardo Ramírez de la Torre
 */
public class SessionValidator {
  public static String TWO_FACTOR_AUTH = "TWO_FACTOR_AUTH";
  public static String ENTERPRISE_ACCOUNT = "ENTERPRISE_ACCOUNT";
  public static String PERSONAL_ACCOUNT = "PERSONAL_ACCOUNT";
  public static String ADMIN_ACCOUNT = "ADMIN_ACCOUNT";
  
  public static boolean valid(String current, String expected) {
    if(current == null || expected == null)
      return false;

    return current.equals(expected);
  }
}
