package br.com.webnize.security.oauth2.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Classe utilitária para operações com JSON objects.
 *
 * @author reginaldo.santos
 * @version 1.0
 * @since 17/05/2016
 */
public class JsonUtil {


	/**
	 * Implementação Jackson para converter JSON string para a Map<String, Object>.
	 *
	 * @param jsonString
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> jsonStringAsMap(String jsonString){

	  Map<String, Object> result = new HashMap<String, Object>();

    ObjectMapper mapper = new ObjectMapper();
    try {
      result = mapper.readValue(jsonString, new TypeReference<Map<String, Object>>(){});
    }
    catch(IOException e) {
      throw new RuntimeException(e);
    }

	  return result;
	}


	/**
   * Implementação Jackson para converter JSON object para a Map<String, Object>.
   *
   * @param jsonString
   * @return Map<String, Object>
   */
	public static Map<String, Object> jsonAsMap(org.json.JSONObject userInfo) {
    return jsonStringAsMap(userInfo.toString());
  }


	/**
	 * Implementação Jackson para converter JSON object para a Map<String, Object>.
	 *
	 * @param jsonString
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> jsonAsMap(org.codehaus.jettison.json.JSONObject userInfo) {
	  return jsonStringAsMap(userInfo.toString());
	}

}
