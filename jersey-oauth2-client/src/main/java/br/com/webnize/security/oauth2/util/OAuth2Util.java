package br.com.webnize.security.oauth2.util;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author reginaldo.santos
 * @version 1.0
 * @since 17/05/2016
 */
public class OAuth2Util {

	public static final String OIDC_USER = "OIDC_USER";

	public static JSONObject getUserInfo(String accessToken)
			throws ClientProtocolException, IOException, JSONException {
		// get User Info
		HttpGet httpMethod = new HttpGet(AuthConfig.PROFILE_URI);
		httpMethod.setHeader("Authorization", "Bearer " + accessToken);

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse httResponse = httpClient.execute(httpMethod);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String outputString = responseHandler.handleResponse(httResponse);

		// Convert JSON response
		return new JSONObject(outputString);
	}

	public static String exceptionToString(Throwable throwable) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		throwable.printStackTrace(pw);
		String title= "";

		if(!(throwable instanceof NullPointerException))
			title = throwable.getCause() == null ? throwable.getMessage() : throwable.getCause().toString();
			else
				title = throwable.getClass().getSimpleName();

		JSONObject jsonObject=new JSONObject();
			try{
				jsonObject.put("status", 500);
				jsonObject.put("title", title);
				jsonObject.put("msg", sw.toString());
			}catch(JSONException json){
				return "{ "
						+"\"status\": 500 , "
						+"\"title\": "+title+" , "
						+"\"msg\": "+sw.toString()+" , "
						+ "}";
			}

		return jsonObject.toString();
	}
}
