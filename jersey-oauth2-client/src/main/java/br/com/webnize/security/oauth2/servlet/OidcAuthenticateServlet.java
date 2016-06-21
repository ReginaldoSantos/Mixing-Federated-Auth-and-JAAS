package br.com.webnize.security.oauth2.servlet;

import java.io.IOException;
import java.security.Principal;
import java.util.UUID;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.glassfish.jersey.client.oauth2.ClientIdentifier;
import org.glassfish.jersey.client.oauth2.OAuth2ClientSupport;
import org.glassfish.jersey.client.oauth2.OAuth2CodeGrantFlow;
import org.glassfish.jersey.client.oauth2.OAuth2CodeGrantFlow.Builder;
import org.glassfish.jersey.client.oauth2.OAuth2CodeGrantFlow.Phase;

import br.com.webnize.security.oauth2.util.AuthConfig;
import br.com.webnize.security.oauth2.util.OverrideablePropertiesFile;


/**
 * Servlet de autenticação mapeando /oauth2/authorize.
 *
 * Em geral, mecanismo será invocado pela tela de login, quando usuário
 * decidir utilizar o Federated Authentication.
 *
 * Além do ENDPOINT de autorização, espera-se que o hyperlink envie como
 * parâmetro qual provedor de identidade deve ser carregado.
 *
 * Ex: http://localhost:8080/oauth2/authorize?idProvider=google
 *
 * Até o momento, valores suportados são ['facebook', 'azure', 'google'].
 *
 * @author reginaldo.santos
 * @version 1.0
 * @since 17/05/2016
 */
@WebServlet(loadOnStartup=0, description="OIDC Authentication Servlet", urlPatterns={"/oauth2/authorize"})
public class OidcAuthenticateServlet extends AbstractOidcServlet {


  /**
   * UID gerado automaticamente e utilizado para serialização.
   */
  private static final long serialVersionUID = -2077591322051264658L;


  /**
   * Classe responsável pela geração de logs.
   */
  private static Logger logger = Logger.getLogger(OidcAuthenticateServlet.class.getName());


  /**
   * Força verificação de autenticação antes de qualquer HTTP Method ser invocado.
   *
   */
  @Override
  public void service(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {

    Principal principal = request.getUserPrincipal();

    if(principal == null) {

      initialize(request);

      logger.finest("[OidcAuthenticateServlet] service : not authorized > attemp to sign in ");
      String authorizationUri = authorize(request);
      response.sendRedirect(authorizationUri);
    }
    else {
      logger.finest("[OidcAuthenticateServlet] service : authorized ");
      super.service(request, response);
    }
  }


  /**
   * Inicia o Authorization Code Flow com o Authorization Server.
   *
   * @param request
   * @return
   */
  private String authorize(HttpServletRequest request) {

    OverrideablePropertiesFile propertyHolder = AuthConfig.getPropertyHolder(idProvider);

    ClientIdentifier clientIdentifier = new ClientIdentifier(
                                          propertyHolder.getProperty(AuthConfig.CLIENT_ID),
                                            propertyHolder.getProperty(AuthConfig.CLIENT_SECRET));

    OAuth2CodeGrantFlow flow; {

      if(AuthConfig.FACEBOOK.equals(idProvider)){
        flow = OAuth2ClientSupport.facebookFlowBuilder(
                clientIdentifier,
                  propertyHolder.getProperty(AuthConfig.CALLBACK_URI))
                    .scope(propertyHolder.getProperty(AuthConfig.SCOPE))
                      .property(Phase.AUTHORIZATION, "display", "popup")
                        .build();
      }
      else if(AuthConfig.GOOGLE.equals(idProvider)){
        flow = OAuth2ClientSupport.googleFlowBuilder(
                clientIdentifier,
                  propertyHolder.getProperty(AuthConfig.CALLBACK_URI),
                    propertyHolder.getProperty(AuthConfig.SCOPE))
                      .build();
      }
      else if(AuthConfig.AZURE.equals(idProvider)){
        Builder<?> builder = OAuth2ClientSupport
                .authorizationCodeGrantFlowBuilder(
                  clientIdentifier,
                    propertyHolder.getProperty(AuthConfig.AUTHORIZATION_URI),
                      propertyHolder.getProperty(AuthConfig.TOKEN_URI));

        flow = builder
          .scope(propertyHolder.getProperty(AuthConfig.SCOPE))
            .property(Phase.AUTHORIZATION, "response_mode", "query")
                .property(Phase.AUTHORIZATION, "nonce", UUID.randomUUID().toString())
                  .property(Phase.AUTHORIZATION, "site_id", "500879")
                    .redirectUri(propertyHolder.getProperty(AuthConfig.CALLBACK_URI))
                      .build();
      }
      else {
        Builder<?> builder = OAuth2ClientSupport
                              .authorizationCodeGrantFlowBuilder(
                                clientIdentifier,
                                  propertyHolder.getProperty(AuthConfig.AUTHORIZATION_URI),
                                    propertyHolder.getProperty(AuthConfig.TOKEN_URI));

        flow = builder
                .scope(propertyHolder.getProperty(AuthConfig.SCOPE))
                  .redirectUri(propertyHolder.getProperty(AuthConfig.CALLBACK_URI))
                    .build();
      }
    }

    request.getSession().setAttribute(AuthConfig.CODE_GRANT_FLOW, flow);

    return flow.start();
  }


  /**
   * Força verificação de autorização a cada GET.
   * Útil se o JAAS Realm for removido.
   *
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
//  @Override
//  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//    String authorizationUri = authorize(request);
//    response.sendRedirect(authorizationUri);
//  }


  /**
   * Força verificação de autorização a cada POST.
   * Útil se o JAAS Realm for removido.
   *
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
//  @Override
//  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//    String authorizationUri = authorize(request);
//    response.sendRedirect(authorizationUri);
//  }

}
