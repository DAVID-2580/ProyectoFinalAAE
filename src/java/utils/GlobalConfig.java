package utils;

/**
 *
 * @author Angel Ricardo Ramírez de la Torre
 */
public class GlobalConfig {
  // GLOBAL
  public static String API_ENDPOINT = "http://localhost";
  public static String ADMIN_EMAIL = "admin@ema.com";
  
  // VIEWS
  public static String INDEX_VIEW = "index.html";
  public static String LOGIN_VIEW = "login.jsp";
  public static String ADMIN_DASHBOARD_VIEW = "admin/index.jsp";
  public static String ENTERPRISE_ACCOUNT_INDEX_VIEW = "enterprise/index.jsp";
  public static String PERSONAL_ACCOUNT_INDEX_VIEW = "personal/index.jsp";
  public static String TWO_FACTOR_AUTH_VIEW = "two-factor-auth.jsp";
  
  // CONTROLLERS
  public static String LOGOUT_CONTROLLER = "logout";
  public static String DASHBOARD_MASTER_CONTROLLER = "dashboard-master";
  public static String ENTERPRISE_ACCOUNT_VERIFIER_CONTROLLER = "verify-account";  
  public static String ACCOUNT_FINDER_CONTROLLER = "account-finder";  
}
