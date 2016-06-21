package br.com.webnize.security.oauth2.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Representação de um properties file.
 *
 * @author reginaldo.santos
 * @version 1.0
 * @since 22/09/2015
 */
public class PropertiesFile {

  private static final Logger LOGGER = Logger.getLogger(PropertiesFile.class.getName());

  private static final String COULD_NOT_READ_PROPERTY_FILE = "Não foi possível ler o arquivo de propriedades: ";

  private static final String READING_PROPERTY_FILE = "Lendo o arquivo de propriedades: ";

  Properties fileProperties = new Properties();

  /**
   * Cria um properties file.
   *
   * @param fileName nome absoluto.
   */
  public PropertiesFile(String fileName) {
    try (InputStream propertiesResource = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
      if (propertiesResource != null) {
        LOGGER.info(READING_PROPERTY_FILE + fileName);
        this.fileProperties.load(propertiesResource);
      }
      else {
        LOGGER.warning(COULD_NOT_READ_PROPERTY_FILE + fileName);
      }
    }
    catch(IOException e) {
      LOGGER.warning(COULD_NOT_READ_PROPERTY_FILE + fileName);
      LOGGER.warning(e.getMessage());
    }
  }

  /**
   * Obtém um parâmetro dado o parameterName.
   *
   * @param parameterName
   * @return
   */
  public String getProperty(String parameterName) {
    return this.fileProperties.getProperty(parameterName);
  }

}
