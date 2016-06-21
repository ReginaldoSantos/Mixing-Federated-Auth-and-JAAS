package br.com.webnize.security.oauth2.service;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import br.com.webnize.security.oauth2.util.AuthConfig;
import br.com.webnize.security.oauth2.util.OverrideablePropertiesFile;


/**
 * Implementação de acesso ao USER_ENDPOINT para obter os dados do usuário autenticado.
 *
 * TODO: Implementar 'Require App Secret' para facebook
 *
 * @author reginaldo.santos
 * @version 1.0
 * @since 17/05/2016
 */
public class UserInfo {


	/**
	 * Acessa o USER_ENDPOINT /userinfo para obter informações do usuário autenticado.
	 *
	 * @param accessToken
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static String getUserInfoAsString(String idProvider, String accessToken) throws ClientProtocolException, IOException, JSONException {

	  OverrideablePropertiesFile propertyHolder = AuthConfig.getPropertyHolder(idProvider);

		HttpGet httpMethod = new HttpGet(propertyHolder.getProperty(AuthConfig.PROFILE_URI));

		httpMethod.setHeader("Authorization", "Bearer " + accessToken);

		if(AuthConfig.AZURE.equals(idProvider)){
		  httpMethod.setHeader("api-version", "1.6");
		  httpMethod.setHeader("Accept", "application/json;odata=minimalmetadata");
		}

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse httResponse = httpClient.execute(httpMethod);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String strUserInfo = responseHandler.handleResponse(httResponse);

		return strUserInfo;
	}


	/**
	 * Acessa o USER_ENDPOINT /userinfo para obter informações do usuário autenticado.
	 *
	 * @param accessToken
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 */
	public static JSONObject getUserInfo(String idProvider, String accessToken) throws ClientProtocolException, IOException, JSONException {
	  String strUserInfo = getUserInfoAsString(idProvider, accessToken);
	  return new JSONObject(strUserInfo);
	}

}
