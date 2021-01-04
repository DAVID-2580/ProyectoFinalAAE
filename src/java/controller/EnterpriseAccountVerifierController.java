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
import model.entities.EnterpriseAccountVerifierEntity;
import utils.GlobalConfig;
import utils.HttpRequestService;

/**
 *
 * @author Angel Ricardo Ramírez de la Torre
 */
@WebServlet(name = "EnterpriseAccountVerifierController", urlPatterns = {"/verify-account"})
public class EnterpriseAccountVerifierController extends HttpServlet {
  private HttpRequestService requestService = new HttpRequestService();
  private EnterpriseAccountVerifierEntity enterpriseVerifierEntity = new EnterpriseAccountVerifierEntity();
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
    EnterpriseAccountVerifierEntity.Response enterpriseVerifierResponse = null;
    boolean verify = request.getParameter("validate").equals("si");
    String accountId = request.getParameter("account_id");
        
    try {
      EnterpriseAccountVerifierEntity.Request enterpriseVerifierRequest = enterpriseVerifierEntity.new Request();      
      enterpriseVerifierRequest.verify = verify;

      String body = gson.toJson(enterpriseVerifierRequest);

      enterpriseVerifierResponse = requestService.put(
              "/account/verify/" + accountId,
              body,
              HttpRequestService.CONTENT_TYPE_JSON,
              EnterpriseAccountVerifierEntity.Response.class
      );

    } catch (Exception ex) {
      enterpriseVerifierResponse = enterpriseVerifierEntity.new Response();
      enterpriseVerifierResponse.error = enterpriseVerifierEntity.new ErrorResponse();
      enterpriseVerifierResponse.error.message = "Internal Error";
      enterpriseVerifierResponse.error.reason = ex.getMessage();
      request.getRequestDispatcher(GlobalConfig.LOGIN_VIEW).forward(request, response);
      return;
    }    
    
    request.setAttribute("infoTitle", "Verificación Correcta");
    request.setAttribute("infoMessage", enterpriseVerifierResponse.success);    
    request.getRequestDispatcher(GlobalConfig.DASHBOARD_MASTER_CONTROLLER).forward(request, response);
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
