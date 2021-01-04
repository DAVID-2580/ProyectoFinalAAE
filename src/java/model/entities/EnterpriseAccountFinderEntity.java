package model.entities;

import java.util.List;

public class EnterpriseAccountFinderEntity {

  public class Request {
  }

  public class Response {

    public SuccessResponse success;
    public ErrorResponse error;
  }

  public class SuccessResponse {
    public List<Account> accounts;
  }
  
  public class Account {
    public String account_id;
    public String email;
    public String firstName;
    public String lastName;
    public String signature;
    public boolean enterprise_account;
  }
  
  public class ErrorResponse {

    public String message;
    public String reason;
  }

}
