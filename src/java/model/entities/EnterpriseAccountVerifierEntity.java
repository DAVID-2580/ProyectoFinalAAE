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
public class EnterpriseAccountVerifierEntity {
  
  public class Request {
    public boolean verify;
  }
  
  public class Response {
    public String success;
    public ErrorResponse error;
  }
  
  public class ErrorResponse {
    public String message;
    public String reason;
  }
  
}
