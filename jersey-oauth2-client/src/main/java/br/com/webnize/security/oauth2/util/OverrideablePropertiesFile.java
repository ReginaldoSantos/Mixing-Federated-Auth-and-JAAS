package br.com.webnize.security.oauth2.util;

/**
 * Um 'properties holder' no qual as propriedades podem ser sobrepostas por 'system properties'.
 *
 * @author reginaldo.santos
 * @version 1.0
 * @since 22/09/2015
 */
public class OverrideablePropertiesFile extends PropertiesFile {

  /**
   * {@inheritDoc}
   */
  public OverrideablePropertiesFile(String fileName) {
    super(fileName);
  }

  /**
   * {@inheritDoc}
   */
  public String getProperty(String propertyKey) {
    String propertyValue = System.getProperty(propertyKey);
    if (propertyValue == null) {
      propertyValue = super.getProperty(propertyKey);
    }
    return propertyValue;
  }

}
