package model.entities;

public class SignatureCreatorEntity {
  public class Request {}
  
  public class Response {
    public SuccessResponse success;
    public ErrorResponse error;
  }
  
  public class SuccessResponse {
    public String id;
    public String message;
  }
  
  public class ErrorResponse {
    public String message;
    public String reason;
  }
}
