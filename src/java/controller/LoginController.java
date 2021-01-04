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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.entities.LoginEntity;
import utils.GlobalConfig;
import utils.HttpRequestService;
import utils.SessionValidator;

@WebServlet(name = "LoginController", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

  private HttpRequestService requestService = new HttpRequestService();
  private LoginEntity loginEntity = new LoginEntity();
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
    LoginEntity.Response loginResponse = null;
    String email = request.getParameter("email"),
           password = request.getParameter("password");
    
    try {
      LoginEntity.Request loginRequest = loginEntity.new Request();

      loginRequest.email = email;
      loginRequest.password = password;
      String body = gson.toJson(loginRequest);

      loginResponse = requestService.post(
              "/credentials/login",
              body,
              HttpRequestService.CONTENT_TYPE_JSON,
              LoginEntity.Response.class
      );

    } catch (Exception ex) {
      loginResponse = loginEntity.new Response();
      loginResponse.error = loginEntity.new ErrorResponse();
      loginResponse.error.message = "Internal Error";
      loginResponse.error.reason = ex.getMessage();
    }

    if (loginResponse.error != null) {
      request.setAttribute("errorTitle", loginResponse.error.message);
      request.setAttribute("errorMessage", loginResponse.error.reason);
      request.getRequestDispatcher(GlobalConfig.LOGIN_VIEW).forward(request, response);
      return;
    }

    if (loginResponse.success.status.equals("NOT_AUTHORIZED")) {
      request.setAttribute("errorTitle", "Credenciales Incorrectas");
      request.setAttribute("errorMessage", "Favor de verificar su email y contraseña");
      request.getRequestDispatcher(GlobalConfig.LOGIN_VIEW).forward(request, response);
      return;
    }
    
    HttpSession session = request.getSession();
    session.setAttribute("email", email);
    
    if (loginResponse.success.status.equals("2FA_REQUIRED")) {
      session.setAttribute("SessionType", SessionValidator.TWO_FACTOR_AUTH);
      request.getRequestDispatcher(GlobalConfig.TWO_FACTOR_AUTH_VIEW).forward(request, response);
      return;
    }
    
    boolean isEnterpriseAccount = loginResponse.success.enterprise_account;    
    session.setAttribute("user_id", loginResponse.success.user_id);
    session.setAttribute("enterprise_account", isEnterpriseAccount);
    
    Cookie userIdCookie = new Cookie("user_id", loginResponse.success.user_id);
    response.addCookie(userIdCookie);
    
    if (isEnterpriseAccount)
      session.setAttribute("SessionType", SessionValidator.ENTERPRISE_ACCOUNT);
    
    else
      session.setAttribute("SessionType", SessionValidator.PERSONAL_ACCOUNT);
    
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
    response.setHeader("Access-Control-Allow-Credentials", "true");
    response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS,HEAD");
    
    request.getRequestDispatcher(GlobalConfig.ACCOUNT_FINDER_CONTROLLER).forward(request, response);
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
