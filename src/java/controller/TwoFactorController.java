/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.entities.TwoFactorEntity;
import utils.GlobalConfig;
import utils.HttpRequestService;
import utils.SessionValidator;

/**
 *
 * @author Angel Ricardo Ramírez de la Torre
 */
@WebServlet(name = "TwoFactorController", urlPatterns = {"/two-factor"})
public class TwoFactorController extends HttpServlet {
  private HttpRequestService requestService = new HttpRequestService();
  private TwoFactorEntity twoFactorEntity = new TwoFactorEntity();
  private Gson gson = new Gson();
  
  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    HttpSession session = request.getSession();
    TwoFactorEntity.Response twoFactorResponse = null;
    String email = (String) session.getAttribute("email"),
           code = request.getParameter("code");
    
    try {
      TwoFactorEntity.Request twoFactorRequest = twoFactorEntity.new Request();

      twoFactorRequest.email = email;
      twoFactorRequest.code = code;
      String body = gson.toJson(twoFactorRequest);

      twoFactorResponse = requestService.post(
              "/credentials/2fa",
              body,
              HttpRequestService.CONTENT_TYPE_JSON,
              TwoFactorEntity.Response.class
      );

    } catch (Exception ex) {
      twoFactorResponse = twoFactorEntity.new Response();
      twoFactorResponse.error = twoFactorEntity.new ErrorResponse();
      twoFactorResponse.error.message = "Internal Error";
      twoFactorResponse.error.reason = ex.getMessage();
    }

    if (twoFactorResponse.error != null) {
      request.setAttribute("errorTitle", twoFactorResponse.error.message);
      request.setAttribute("errorMessage", twoFactorResponse.error.reason);
      request.getRequestDispatcher(GlobalConfig.TWO_FACTOR_AUTH_VIEW).forward(request, response);
      return;
    }

    if (twoFactorResponse.success.status.equals("FAILED")) {
      request.setAttribute("errorTitle", "Código de verificación incorrecto");
      request.setAttribute("errorMessage", "Favor de verificar que el código este actualizado");
      request.getRequestDispatcher(GlobalConfig.TWO_FACTOR_AUTH_VIEW).forward(request, response);
      return;
    }
        
    session.setAttribute("email", email);
    
    boolean isEnterpriseAccount = twoFactorResponse.success.enterprise_account;
    session.setAttribute("user_id", twoFactorResponse.success.user_id);
    session.setAttribute("enterprise_account", isEnterpriseAccount);
    
    if(email.equals(GlobalConfig.ADMIN_EMAIL)) {
      session.setAttribute("SessionType", SessionValidator.ADMIN_ACCOUNT);
      request.getRequestDispatcher(GlobalConfig.DASHBOARD_MASTER_CONTROLLER).forward(request, response);
      //request.getRequestDispatcher(GlobalConfig.ADMIN_DASHBOARD_VIEW).forward(request, response);
      return;
    }
    
    if (isEnterpriseAccount) {
      session.setAttribute("SessionType", SessionValidator.ENTERPRISE_ACCOUNT);
      request.getRequestDispatcher(GlobalConfig.ENTERPRISE_ACCOUNT_INDEX_VIEW).forward(request, response);      
    }      
    else {
      session.setAttribute("SessionType", SessionValidator.PERSONAL_ACCOUNT);
      request.getRequestDispatcher(GlobalConfig.PERSONAL_ACCOUNT_INDEX_VIEW).forward(request, response);
    }      
  }

  // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
  /**
   * Handles the HTTP <code>GET</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    processRequest(request, response);
  }

  /**
   * Handles the HTTP <code>POST</code> method.
   *
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    processRequest(request, response);
  }

  /**
   * Returns a short description of the servlet.
   *
   * @return a String containing servlet description
   */
  @Override
  public String getServletInfo() {
    return "Short description";
  }// </editor-fold>

}
