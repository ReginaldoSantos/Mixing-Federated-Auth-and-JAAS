package br.com.webnize.security.oauth2.servlet;

import java.io.IOException;
import java.security.Principal;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.glassfish.jersey.client.oauth2.OAuth2CodeGrantFlow;
import org.glassfish.jersey.client.oauth2.TokenResult;

import br.com.webnize.security.oauth2.util.JAASCredentials;
import br.com.webnize.security.oauth2.util.OAuth2Util;

/**
 * Servlet para Callback de autenticação (Code Grant Flow) que pode ser declarada
 * no descritor da aplicação (web.xml) ou ainda extendida por uma classe anotada
 * com <code>@WebServlet</code>.
 *
 * @author reginaldo.santos
 * @version 1.0
 * @since 27/11/2015
 */
@WebServlet(loadOnStartup=1, description="OIDC Callback Servlet", urlPatterns={"/oauth2/callback"})
public class OidcCallbackServlet extends HttpServlet {


  /**
   * UID gerado automaticamente e utilizado para serialização.
   */
  private static final long serialVersionUID = -696840155622416565L;


  /**
   * Classe responsável pela geração de logs.
   */
  private static Logger logger = Logger.getLogger(OidcCallbackServlet.class.getName());


  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    Principal principal = request.getUserPrincipal();

    if(principal == null) {

      logger.finest("[OidcCallbackServlet] doGet : not authorized yet > processing callback code flow ");

      String code = request.getParameter("code");
      String state = request.getParameter("state");
      String accessToken = callback(request, code, state);

      request.getSession().setAttribute("accessToken", accessToken);

      //response.setContentType("application/json");

      ServletOutputStream out = response.getOutputStream();

      try {

        JSONObject userInfo = OAuth2Util.getUserInfo(accessToken);

        JAASCredentials userCredentials = getUserCredentials(userInfo);

        request.login(userCredentials.getUsername(), userCredentials.getPassword());

        principal = request.getUserPrincipal();

        out.print(String.format("usuário autenticado: %s (className => %s)",
          principal.getName(),
          principal.getClass().getName()
        ));
      }
      catch(IOException | JSONException exception) {
        out.print(OAuth2Util.exceptionToString(exception));
      }
    }
    else {
      logger.finest("[OidcCallbackServlet] doGet : authorized > redirecting to original URL ");
    }

    //response.sendRedirect(Globals.getUrlStart(request.getContextPath()));
  }


  public String callback(HttpServletRequest request, @QueryParam("code") String code, @QueryParam("state") String state) {
    final OAuth2CodeGrantFlow flow = (OAuth2CodeGrantFlow)request.getSession().getAttribute("OAuth2CodeGrantFlow");
    final TokenResult tokenResult = flow.finish(code, state);

    return tokenResult.getAccessToken();
  }

  private JAASCredentials getUserCredentials(JSONObject userInfo) throws JSONException {

    logger.finest(String.format("Username: ", userInfo.getString("name")));

    return new JAASCredentials("reginaldo.santos", "password");
  }

}
