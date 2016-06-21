package br.com.webnize.security.oauth2.servlet;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.webnize.security.oauth2.util.AuthConfig;


/**
 * Servlet Abstrata para centralizar métodos da autenticação (Code Grant Flow) que utilizam
 * os objetos HttpRequestServlet, HttpResponseServlet e HttpSession.
 *
 * @author reginaldo.santos
 * @version 1.0
 * @since 27/11/2015
 */
public abstract class AbstractOidcServlet extends HttpServlet {


  /**
   * UID gerado automaticamente e utilizado para serialização.
   */
  private static final long serialVersionUID = -3131872761638215635L;


  /**
   * Identificador do Identity Provider.
   */
  protected String idProvider;
  protected String originalURL;


  /**
   * Inicializa os valores de idProvider e originalURL.
   *
   * @param request
   */
  protected void initialize(HttpServletRequest request){
    this.idProvider  = getIdProvider(request);
    this.originalURL = getOriginalURL(request);

    request.getSession().setAttribute(AuthConfig.ORIGINAL_URL_KEY, originalURL);
    request.getSession().setAttribute(AuthConfig.ID_PROVIDER_KEY,  idProvider);
  }


  /**
   * Retorna a URL original guardada nesta sessão.
   *
   * @param request
   * @return
   */
  protected String getOriginalURL(HttpSession session) {
    String originalURL = (String)session.getAttribute(AuthConfig.ORIGINAL_URL_KEY);
    return originalURL;
  }


  /**
   * Retorna a URL original utilizada no request iniciado pelo usuário
   * ou URL default do sistema caso o primeiro seja nulo.
   *
   * @param request
   * @return
   */
  protected String getOriginalURL(HttpServletRequest request) {
    String originalURL = request.getParameter(AuthConfig.ORIGINAL_URL_KEY);
    return originalURL != null ? originalURL : request.getContextPath();
  }



  /**
   * Retorna o valor atribuido à idProvider na sessão.
   *
   * @param request
   * @return
   */
  protected String getIdProvider(HttpSession session) {
    String idProvider = (String)session.getAttribute(AuthConfig.ID_PROVIDER_KEY);

    validateIdProvider(idProvider);

    return idProvider;
  }


  /**
   * Retorna o valor atribuido à idProvider na query String.
   *
   * @param request
   * @return
   */
  protected String getIdProvider(HttpServletRequest request) {
    String idProvider = request.getParameter(AuthConfig.ID_PROVIDER_KEY);

    validateIdProvider(idProvider);

    return idProvider;
  }


  /**
   * Verificar se idProvider é válido.
   *
   * @param idProvider
   */
  private void validateIdProvider(String idProvider){
    if(idProvider == null || "".equals(idProvider)){
      throw new RuntimeException("Não foi possível identificar o Identity Provider.");
    }

    if(!AuthConfig.idProviders.contains(idProvider)){
      throw new RuntimeException(String.format("Identity Provider %s não suportado.", idProvider));
    }
  }


  /**
   * Dada uma URL retorna um Map<String, String> contendos todos os parâmetros da query String
   * separados por chave e valor.
   *
   * @param url
   * @return
   * @throws UnsupportedEncodingException
   */
  public static Map<String, String> getSplittedQueryString(URL url) throws UnsupportedEncodingException {
    Map<String, String> parameters = new LinkedHashMap<String, String>();
    String query = url.getQuery();

    if(query != null){
      String[] pairs = query.split("&");

      for (String pair : pairs) {
        int idx = pair.indexOf("=");
        parameters.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
      }
    }

    return parameters;
  }

}
