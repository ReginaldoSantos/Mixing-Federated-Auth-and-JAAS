package techne.web.security.oauth2.util;

/**
 * Pojo para transferência interna (CronosFramework e Aplicação) das credenciais
 * do usuário no seu SecurityConnection provider.
 *
 * Necessário quando a autenticação é externa (Federated Authentication), mas a
 * autorização continua sendo interna via JAAS.
 *
 * @author Techne
 * @version 1.0
 * @since 17/05/2016
 */
public class JAASCredentials {

  private String username;
  private String password;

  public JAASCredentials(String username, String password){
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

}
