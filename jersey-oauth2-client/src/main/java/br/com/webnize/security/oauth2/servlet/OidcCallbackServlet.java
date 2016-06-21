package br.com.webnize.security.oauth2.servlet;

import java.io.IOException;
import java.net.URL;
import java.security.Principal;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.QueryParam;

import org.codehaus.jettison.json.JSONException;
import org.glassfish.jersey.client.oauth2.OAuth2CodeGrantFlow;
import org.glassfish.jersey.client.oauth2.TokenResult;

import br.com.webnize.security.oauth2.service.UserInfo;
import br.com.webnize.security.oauth2.util.AuthConfig;
import br.com.webnize.security.oauth2.util.JAASCredentials;
import br.com.webnize.security.oauth2.util.JsonUtil;


/**
 * Servlet para Callback de autenticação (Code Grant Flow), mapeando /oauth2/callback.
 *
 * Recebe o retorno do Federated Identity Provider.
 *
 * @author reginaldo.santos
 * @version 1.0
 * @since 27/11/2015
 */
@WebServlet(loadOnStartup=1, description="OIDC Callback Servlet", urlPatterns={"/oauth2/callback"})
public class OidcCallbackServlet extends AbstractOidcServlet {


  /**
   * UID gerado automaticamente e utilizado para serialização.
   */
  private static final long serialVersionUID = -696840155622416565L;


  /**
   * Classe responsável pela geração de logs.
   */
  private static Logger logger = Logger.getLogger(OidcCallbackServlet.class.getName());


  private HttpSession session;


  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    String originalURL = getOriginalURL(request.getSession());

    Principal principal = request.getUserPrincipal();

    if(principal == null) {

      String idProvider = getIdProvider(request.getSession());

      logger.finest("[OidcCallbackServlet] doGet : not authorized yet > processing callback code flow ");

      String code = request.getParameter("code");
      String state = request.getParameter("state");
      Map<String, Object> tokenResult = callback(request, code, state);

      try {
        Map<String, Object> userInfoMap = JsonUtil.jsonAsMap(UserInfo.getUserInfo(idProvider, (String)tokenResult.get("access_token")));

        JAASCredentials userCredentials = getJAASCredentials(userInfoMap, idProvider);

        if(userCredentials == null) {
          throw new RuntimeException("[JAASCredentials] Credencial de autorização inválida.");
        }

        request.login(userCredentials.getUsername(), userCredentials.getPassword());

        /*
         * Uma vez autenticado os objetos pertinentes a autenticação são guardados na sessão.
         */

        session = request.getSession();

        session.setAttribute(AuthConfig.ID_PROVIDER_KEY,  idProvider);
        session.setAttribute(AuthConfig.TOKEN_RESULT_KEY, tokenResult);
        session.setAttribute(AuthConfig.USERINFO_KEY,     userInfoMap);
      }
      catch(Exception e) {

        if(e instanceof ServletException && e.getMessage().contains("Login failed")){
          logger.severe(String.format("Autentição com %s ocorreu com sucesso, mas não há JAAS Realm definido.", idProvider));
        }
        else {

          request.logout();

          /*
           * Manda reposta para o default error page do JAAS Login Module.
           *
           * Neste caso: /login.jsp?error=true
           *
           */

          Map<String, String> parameters = getSplittedQueryString(new URL(originalURL));

          if(!parameters.containsKey("error")){
            if(originalURL.contains("?")){
              originalURL = originalURL + "&error=true";
            }
            else {
              originalURL = originalURL + "?error=true";
            }
          }

          logger.throwing(OidcCallbackServlet.class.getName(), "doGet(...)", e);
        }
      }
    }
    else {
      logger.finest("[OidcCallbackServlet] doGet : authorized > redirecting to original URL ");
    }

    response.sendRedirect(originalURL);

  }


  public Map<String, Object> callback(HttpServletRequest request, @QueryParam("code") String code, @QueryParam("state") String state) {
    OAuth2CodeGrantFlow flow = (OAuth2CodeGrantFlow)request.getSession().getAttribute(AuthConfig.CODE_GRANT_FLOW);

    final TokenResult tokenResult = flow.finish(code, state);

    return tokenResult.getAllProperties();
  }


  /**
   * Chama a implementação da aplicação para obter as credenciais do usuário no JAAS Realm.
   *
   * Esperasse que a aplicação consiga fazer um 'match' entre as informações do usuário no
   * Identity Provider e a sua representação local do usuário.
   *
   * @param userInfo
   * @param idProvider
   * @return
   * @throws JSONException
   */
  private JAASCredentials getJAASCredentials(Map<String, Object> userInfoMap, String idProvider) {

    JAASCredentials jaasCredentials;

    try {
      logger.finest(String.format("Obtendo autorização do sistema para usuário '%s'", userInfoMap.get("name")));

      /*
       * Deveríamos ter aqui um interface com o JAAS e no lado do Realm faríamos a implementação para buscar as credenciais do usuário.
       */
      jaasCredentials = new JAASCredentials("reginaldo.santos@webnize.com.br","password");

    }
    catch(Exception e) {
      throw new RuntimeException(e);
    }

    return jaasCredentials;
  }
}
