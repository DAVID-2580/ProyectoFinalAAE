package controller;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.entities.RegisterEntity;
import model.entities.SignatureCreatorEntity;
import utils.GlobalConfig;
import utils.HttpRequestService;
import utils.SessionValidator;

/**
 *
 * @author Angel Ricardo Ramírez de la Torre
 */
@WebServlet(name = "RegisterController", urlPatterns = {"/register"})
public class RegisterController extends HttpServlet {

  private HttpRequestService requestService = new HttpRequestService();
  private RegisterEntity registerEntity = new RegisterEntity();
  private SignatureCreatorEntity signatureCreatorEntity = new SignatureCreatorEntity();
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

    RegisterEntity.Response registerResponse = null;
    String email = request.getParameter("email"),
            firstName = request.getParameter("firstName"),
            lastName = request.getParameter("lastName"),
            password = request.getParameter("password"),
            isEntepriseAccount = request.getParameter("is-enterprise-account");

    try {
      RegisterEntity.Request registerRequest = registerEntity.new Request();
      SignatureCreatorEntity.Request signatureRequest = signatureCreatorEntity.new Request();
      SignatureCreatorEntity.Response signatureResponse
              = requestService.get(
                      "/account/signature/create",
                      HttpRequestService.CONTENT_TYPE_PLAIN,
                      SignatureCreatorEntity.Response.class
              );
      
      registerRequest.email = email;
      registerRequest.firstName = firstName;
      registerRequest.lastName = lastName;
      registerRequest.password = password;
      registerRequest.signature = signatureResponse.error == null ? signatureResponse.success.id : randomString();
      registerRequest.enterprise_account = isEntepriseAccount.equals("true");
      String body = gson.toJson(registerRequest);

      registerResponse = requestService.post(
              "/credentials/register",
              body,
              HttpRequestService.CONTENT_TYPE_JSON,
              RegisterEntity.Response.class
      );

    } catch (Exception ex) {
      registerResponse = registerEntity.new Response();
      registerResponse.error = registerEntity.new ErrorResponse();
      registerResponse.error.message = "Internal Error";
      registerResponse.error.reason = ex.getMessage();
    }

    if (registerResponse.error != null) {
      request.setAttribute("errorTitle", registerResponse.error.message);
      request.setAttribute("errorMessage", registerResponse.error.reason);
      request.getRequestDispatcher(GlobalConfig.LOGIN_VIEW).forward(request, response);
      return;
    }
    
    request.setAttribute("infoTitle", "Registro Correcto");
    request.setAttribute("infoMessage", "La cuenta con email: " + email + ", ha quedado registrada");
    
    request.getRequestDispatcher(GlobalConfig.LOGIN_VIEW).forward(request, response);    
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

  private String randomString() {
    int leftLimit = 97; // letter 'a'
    int rightLimit = 122; // letter 'z'
    int targetStringLength = 10;
    Random random = new Random();

    String generatedString = random.ints(leftLimit, rightLimit + 1)
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();

    return generatedString;
  }
}
