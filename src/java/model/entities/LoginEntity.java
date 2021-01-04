package model.entities;

/**
 *
 * @author Angel Ricardo Ramírez de la Torre
 */
public class LoginEntity {

  public class Request {
    public String email;
    public String password;
  }
  
  public class Response {
    public SuccessResponse success;
    public ErrorResponse error;
  }
  
  public class SuccessResponse {
    public String status;
    public String user_id;
    public boolean enterprise_account;
  }
  
  public class ErrorResponse {
    public String message;
    public String reason;
  }
}
