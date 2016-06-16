package techne.web.security.oauth2.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import techne.web.security.oauth2.util.AuthConfig;


/**
 *
 * @author Techne
 * @version 1.0
 * @since 17/05/2016
 */
@WebServlet("/revoke")
public class RevokeServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

	public String revoke(String accessToken) throws ClientProtocolException, IOException {

		HttpPost httpMethod = new HttpPost(AuthConfig.REVOKE_URI);

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("token", accessToken));
		parameters.add(new BasicNameValuePair("token_type_hint", "access_token"));

		httpMethod.setEntity(new UrlEncodedFormEntity(parameters));

		String basicAuthHeader = Base64.getEncoder().encodeToString((AuthConfig.CLIENT_ID + ":" + AuthConfig.CLIENT_SECRET).getBytes());
		httpMethod.setHeader("Authorization", "Basic " + basicAuthHeader);

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		HttpResponse httResponse = httpClient.execute(httpMethod);

		if (httResponse.getStatusLine().getStatusCode() != 200) {
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			return responseHandler.handleResponse(httResponse);
		}

		return null;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	  String accessToken = request.getSession().getAttribute("accessToken").toString();

		String message = revoke(accessToken);

		if (message == null) {
			request.getSession().setAttribute("accessToken", null);
			response.sendRedirect(request.getContextPath());
		}
		else {
			response.getOutputStream().print(message);
		}
	}

}
