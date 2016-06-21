package br.com.webnize.security.oauth2.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


/**
 * Representa os arquivos de propriedades de autenticação <idProvider>.properties
 *
 * No momento desta implementação eram suportados somente: facebook, azure e google.
 *
 * A implementação baseada no 'OverrideablePropertiesFile' permite que as propriedades
 * possam ser sobrepostas por variáveis do sistema.
 *
 * @author reginaldo.santos
 * @version 1.0
 * @since 17/05/2016
 */
public class AuthConfig {


  /**
   * Classe responsável pela geração de logs.
   */
  private static java.util.logging.Logger logger = Logger.getLogger(AuthConfig.class.getName());


  /*
   * Properties Keys
   */
  public static final String CLIENT_ID         = "CLIENT_ID";
  public static final String CLIENT_SECRET     = "CLIENT_SECRET";
  public static final String AUTHORIZATION_URI = "AUTHORIZATION_URI";
  public static final String SCOPE             = "SCOPE";
  public static final String CALLBACK_URI      = "CALLBACK_URI";
  public static final String TOKEN_URI         = "TOKEN_URI";
  public static final String PROFILE_URI       = "PROFILE_URI";
  public static final String REVOKE_URI        = "REVOKE_URI";
  public static final String GRANT_TYPE        = "GRANT_TYPE";


  /*
   * Query String Constants.
   */
  public static final String ID_PROVIDER_KEY   = "idProvider";
  public static final String ORIGINAL_URL_KEY  = "originalURL";
  public static final String USERINFO_KEY      = "userInfo";
  public static final String TOKEN_RESULT_KEY  = "tokenResultMap";


  /**
   * Apenas o Code Grant Flow é suportado no momento.
   */
  public static final String CODE_GRANT_FLOW   = "OAuth2CodeGrantFlow";

  public static final String FACEBOOK = "facebook";
  public static final String AZURE    = "azure";
  public static final String GOOGLE   = "google";

  public static Map<String, OverrideablePropertiesFile> config = new LinkedHashMap<String, OverrideablePropertiesFile>();

  public static final List<String> idProviders = new ArrayList<String>(Arrays.asList(FACEBOOK, AZURE, GOOGLE));

  static {
    initializeOAuthProviderConfiguration();
  }

  private static void initializeOAuthProviderConfiguration() {
    loadIdProviderConfiguration(AuthConfig.FACEBOOK);
    loadIdProviderConfiguration(AuthConfig.AZURE);
    loadIdProviderConfiguration(AuthConfig.GOOGLE);
  }

  /**
   * Realiza leitura do respectivo arquivo de properties do Identity Provider.
   *
   * {idProvider}.properties
   *
   * @param idProvider
   */
  public static void loadIdProviderConfiguration(String idProvider) {
	  if(idProviders.contains(idProvider)){
	    config.put(idProvider, new OverrideablePropertiesFile(idProvider + ".properties"));
	  }
	  else {
	    logger.warning(String.format("Identity Provider '%s' não suportado.", idProvider));
	  }
  }


	/**
	 * Obtém o OverrideablePropertiesFile referente ao provedor de identidade identificado por idProvider.
	 *
	 * @param idProvider
	 * @return OverrideablePropertiesFile
	 */
	public static OverrideablePropertiesFile getPropertyHolder(String idProvider){
	  OverrideablePropertiesFile propertyHolder = config.get(idProvider);
	  return propertyHolder;
	}

}
