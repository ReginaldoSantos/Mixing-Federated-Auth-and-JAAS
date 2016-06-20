package br.com.webnize.security.oauth2.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;


/**
 * Representa o arquivo de propriedades de autenticação oauth.properties
 *
 * @author reginaldo.santos
 * @version 1.0
 * @since 17/05/2016
 */
public class AuthConfig {

  private static java.util.logging.Logger logger = Logger.getLogger(AuthConfig.class.getName());

  public static String CLIENT_ID;
  public static String CLIENT_SECRET;
  public static String AUTHORIZATION_URI;
  public static String SCOPE;
  public static String CALLBACK_URI;
  public static String TOKEN_URI;
  public static String PROFILE_URI;
  public static String REVOKE_URI;
  public static String GRANT_TYPE;

//  public String CLIENT_ID;
//  public String CLIENT_SECRET;
//  public String AUTHORIZATION_URI;
//  public String SCOPE;
//  public String CALLBACK_URI;
//  public String TOKEN_URI;
//  public String PROFILE_URI;
//  public String REVOKE_URI;
//  public String GRANT_TYPE;
//
//  public static final String FACEBOOK = "facebook";
//  public static final String AZURE    = "azure";
//  public static final String GOOGLE   = "google";
//
//  public static AuthConfig facebook = new AuthConfig(FACEBOOK);
//  public static AuthConfig azure    = new AuthConfig(AZURE);
//  public static AuthConfig google   = new AuthConfig(GOOGLE);
//
//
//	private AuthConfig(String idProvider) {
//	  loadOAuthConfiguration(idProvider);
//  }

  static{
    loadOAuthConfiguration("google");
  }

  /**
	 * Carregar configurações
	 *
	 * @param resourceName
	 *            Nome do Recurso (parâmetro possibilita uso em testes)
	 */
	public static void loadOAuthConfiguration(String resourceName) {
		try {

			InputStream resource = AuthConfig.class.getResourceAsStream("/" + resourceName + ".properties");

			if(resource != null){

				Properties properties = new Properties();
				properties.load(resource);

				CLIENT_ID = properties.getProperty("CLIENT_ID");
				CLIENT_SECRET = properties.getProperty("CLIENT_SECRET");
				AUTHORIZATION_URI = properties.getProperty("AUTHORIZATION_URI");
				SCOPE = properties.getProperty("SCOPE");
				CALLBACK_URI = properties.getProperty("CALLBACK_URI");
				TOKEN_URI = properties.getProperty("TOKEN_URI");
				PROFILE_URI = properties.getProperty("PROFILE_URI");
				REVOKE_URI = properties.getProperty("REVOKE_URI");

				/*
				 * Utilizando grant type authorization_code (OAuth 2.0)
				 *
				 * GRANT_TYPE = properties.getProperty("GRANT_TYPE");
				 *
				 */

				GRANT_TYPE = "authorization_code";

			}
		} catch (IOException e) {
			logger.throwing(AuthConfig.class.getName(), "init()", e);
		}
	}

}
