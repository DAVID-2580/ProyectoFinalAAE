/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.entities.EnterpriseAccountFinderEntity;
import utils.GlobalConfig;
import utils.HttpRequestService;

/**
 *
 * @author Angel Ricardo Ramírez de la Torre
 */
@WebServlet(name = "DashboardMasterController", urlPatterns = {"/dashboard-master"})
public class DashboardMasterController extends HttpServlet {

  private HttpRequestService requestService = new HttpRequestService();
  private EnterpriseAccountFinderEntity enterpriseFinderEntity = new EnterpriseAccountFinderEntity();
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
    EnterpriseAccountFinderEntity.Response enterpriseFinderResponse = null;

    try {
      EnterpriseAccountFinderEntity.Request enterpriseFinderRequest = enterpriseFinderEntity.new Request();

      String body = gson.toJson(enterpriseFinderRequest);

      enterpriseFinderResponse = requestService.get(
              "/account/find/pending-review",
              HttpRequestService.CONTENT_TYPE_PLAIN,
              EnterpriseAccountFinderEntity.Response.class
      );

    } catch (Exception ex) {
      enterpriseFinderResponse = enterpriseFinderEntity.new Response();
      enterpriseFinderResponse.error = enterpriseFinderEntity.new ErrorResponse();
      enterpriseFinderResponse.error.message = "Internal Error";
      enterpriseFinderResponse.error.reason = ex.getMessage();
      request.getRequestDispatcher(GlobalConfig.LOGIN_VIEW).forward(request, response);
      return;
    }

    List<EnterpriseAccountFinderEntity.Account> enterpriseAccounts 
            = enterpriseFinderResponse.success.accounts;

    request.setAttribute("enterpriseAccounts", enterpriseAccounts);
    request.getRequestDispatcher(GlobalConfig.ADMIN_DASHBOARD_VIEW).forward(request, response);
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
