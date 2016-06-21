package br.com.webnize.security.oauth2.util;

/**
 * Pojo para transferência interna das credenciais do usuário no seu JAAS Realm.
 *
 * Necessário quando a autenticação é externa (Federated Authentication), mas a
 * autorização continua sendo interna via JAAS.
 *
 * @author reginaldo.santos
 * @version 1.0
 * @since 17/05/2016
 */
public class JAASCredentials {

  private String username;
  private String password;

  public JAASCredentials(String username, String password){

    if (isEmpty(username)) {
      throw new IllegalArgumentException("username é nulo ou vazio");
    }
    if (isEmpty(password)) {
        throw new IllegalArgumentException("password é nulo ou vazio");
    }

    this.username = username;
    this.password = password;
  }

  private boolean isEmpty(String value){
    if(value == null || "".equals(value)){
      return true;
    }

    return false;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

}
