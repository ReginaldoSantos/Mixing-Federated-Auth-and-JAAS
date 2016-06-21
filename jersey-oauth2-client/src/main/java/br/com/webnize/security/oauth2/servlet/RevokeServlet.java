package br.com.webnize.security.oauth2.servlet;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import br.com.webnize.security.oauth2.util.AuthConfig;
import br.com.webnize.security.oauth2.util.OverrideablePropertiesFile;


/**
 * Servlet para revogação de autenticação mapeando /oauth2/revoke.
 *
 * @author reginaldo.santos
 * @version 1.0
 * @since 17/05/2016
 */
@WebServlet(loadOnStartup=1, description="OIDC Revoke Servlet", urlPatterns={"/oauth2/revoke"})
public class RevokeServlet extends HttpServlet {


  /**
   * UID gerado automaticamente e utilizado para serialização.
   */
  private static final long serialVersionUID = -6598921918912882591L;


  /**
   * Classe responsável pela geração de logs.
   */
  private static Logger logger = Logger.getLogger(RevokeServlet.class.getName());


  /**
   * Revoga a autenticação do objeto Principal passado na sessão.
   *
   * @param principal
   * @return
   * @throws ClientProtocolException
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  private String revoke(HttpServletRequest request) throws ClientProtocolException, IOException {

    HttpSession session = request.getSession();

    Principal principal = request.getUserPrincipal();

    String idProvider  = (String) session.getAttribute(AuthConfig.ID_PROVIDER_KEY);

    String accessToken;{
      Map<String, Object> tokenResult = (Map<String, Object>) session.getAttribute(AuthConfig.TOKEN_RESULT_KEY);
      accessToken = (String) tokenResult.get("access_token");
    }

    Map<String, Object> userInfoMap  = (Map<String, Object>) session.getAttribute(AuthConfig.USERINFO_KEY);

    logger.finest(String.format("Revogando autorização do sistema para usuário '%s'", userInfoMap.get("name")));

    OverrideablePropertiesFile propertyHolder = AuthConfig.getPropertyHolder(idProvider);

    HttpRequestBase httpMethod;{

      if(AuthConfig.FACEBOOK.equals(idProvider)){

        /*
         * Facebook não disponibiliza um REVOKE_URI endpoint.
         * Para revogar a autenticação, deve-se fazer um delete no /permissions.
         */

        httpMethod = new HttpDelete(propertyHolder.getProperty(AuthConfig.REVOKE_URI) + "/" + userInfoMap.get("id") + "/permissions");
        httpMethod.setHeader("Authorization", "Bearer " + accessToken);
      }
      else {

        httpMethod = new HttpPost(propertyHolder.getProperty(AuthConfig.REVOKE_URI));

        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("token", accessToken));
        parameters.add(new BasicNameValuePair("token_type_hint", "access_token"));

        ((HttpPost)httpMethod).setEntity(new UrlEncodedFormEntity(parameters));

        String basicAuthHeader = getBase64EncodedString((
                                   propertyHolder.getProperty(AuthConfig.CLIENT_ID) + ":" +
                                     propertyHolder.getProperty(AuthConfig.CLIENT_SECRET)).getBytes("UTF-8"));

        httpMethod.setHeader("Authorization", "Basic " + basicAuthHeader);
      }
    }

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		HttpResponse httResponse = httpClient.execute(httpMethod);

		if (httResponse.getStatusLine().getStatusCode() != 200) {
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			return responseHandler.handleResponse(httResponse);
		}

		return null;
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	  Principal principal = request.getUserPrincipal();

	  if(principal != null){
  		String message = revoke(request);

  		if (message == null) {
  			request.logout();
  			response.sendRedirect(request.getContextPath());
  		}
  		else {
  			response.getOutputStream().print(message);
  		}
	  }
	}


	/**
	 * Alternativa ao Base64.getEncoder().encodeToString(String) do Java 8, válida para Java 6 e 7.
	 *
	 * @param message
	 * @return Base64 encoded String
	 */
	private static String getBase64EncodedString(byte[] message){
	  return DatatypeConverter.printBase64Binary(message);
	}

}
