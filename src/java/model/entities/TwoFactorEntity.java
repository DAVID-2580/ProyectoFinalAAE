package model.entities;

public class TwoFactorEntity {
  public class Request {
    public String email;
    public String code;
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
