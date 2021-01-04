/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.entities.AccountFinderEntity;
import utils.GlobalConfig;
import utils.HttpRequestService;

/**
 *
 * @author Angel Ricardo Ramírez de la Torre
 */
@WebServlet(name = "AccountFinderController", urlPatterns = {"/account-finder"})
public class AccountFinderController extends HttpServlet {

  private HttpRequestService requestService = new HttpRequestService();
  private AccountFinderEntity finderEntity = new AccountFinderEntity();
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
    
    String userId = (String) session.getAttribute("user_id");
    AccountFinderEntity.Response finderResponse = null;

    try {
      finderResponse = requestService.get(
              "/account/find/" + userId,
              HttpRequestService.CONTENT_TYPE_PLAIN,
              AccountFinderEntity.Response.class
      );

    } catch (Exception ex) {
      finderResponse = finderEntity.new Response();
      finderResponse.error = finderEntity.new ErrorResponse();
      finderResponse.error.message = "Internal Error";
      finderResponse.error.reason = ex.getMessage();
      request.getRequestDispatcher(GlobalConfig.LOGIN_VIEW).forward(request, response);
      return;
    }
    
    session.setAttribute("email", finderResponse.success.email);
    session.setAttribute("firstName", finderResponse.success.firstName);
    session.setAttribute("lastName", finderResponse.success.lastName);
    session.setAttribute("signature", finderResponse.success.signature);
    session.setAttribute("enterprise_account", finderResponse.success.enterprise_account);
    
    boolean isEnterpriseAccount = finderResponse.success.enterprise_account;

    if (isEnterpriseAccount)
      request.getRequestDispatcher(GlobalConfig.ENTERPRISE_ACCOUNT_INDEX_VIEW).forward(request, response);
    else
      request.getRequestDispatcher(GlobalConfig.PERSONAL_ACCOUNT_INDEX_VIEW).forward(request, response);
    
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
