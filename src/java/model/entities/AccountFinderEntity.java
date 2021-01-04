package model.entities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Angel Ricardo Ramírez de la Torre
 */
public class AccountFinderEntity {
  public class Request {
  }
  
  public class Response {
    public SuccessResponse success;
    public ErrorResponse error;
  }
  
  public class SuccessResponse {
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
