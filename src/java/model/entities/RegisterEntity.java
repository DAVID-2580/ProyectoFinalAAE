/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.entities;

/**
 *
 * @author Angel Ricardo Ramírez de la Torre
 */
public class RegisterEntity {

  public class Request {

    public String email;
    public String firstName;
    public String lastName;
    public String password;
    public String signature;
    public boolean enterprise_account;
  }

  public class Response {

    public SuccessResponse success;
    public ErrorResponse error;
  }

  public class SuccessResponse {
    public String user_id;
    public String message;
  }

  public class ErrorResponse {

    public String message;
    public String reason;
  }
}
