# Cronos OAuth2 Client

API baseada na biblioteca [Jersey Security: OAuth2 Support](https://jersey.java.net/documentation/latest/user-guide.html#d0e12830) e que implementa o _"Authorization Code Grant Flow"_.


## Google

Registrar aplica&ccedil;&atilde;o no [Google Developers](https://console.developers.google.com) afim de obter os dados necess&aacute;rios autentica&ccedil;&atilde;o.

Aplicar configura&ccedil;&otilde;es no arquivo _google.properties_ da aplica&ccedil;&atilde;o.

Os seguintes dados s&atilde;o valores default utilizados em testes:

```
Redirect URIs:       http://localhost:8080/oauth2/callback
JavaScript origins:  http://localhost:8080
```


## Facebook

Registrar aplica&ccedil;&atilde;o no [Facebook Developers](https://developers.facebook.com/apps)  e adicionar produto 'Facebook Login' afim de obter os dados necess&aacute;rios autentica&ccedil;&atilde;o.

Aplicar configura&ccedil;&otilde;es no arquivo _facebook.properties_ da aplica&ccedil;&atilde;o.

Os seguintes dados s&atilde;o valores default utilizados em testes:

```
Valid OAuth redirect URIs: http://localhost:8080/oauth2/callback
```


## Azure AD

*TBD*

Registrar aplica&ccedil;&atilde;o no [Azure Developers](https://) afim de obter os dados necess&aacute;rios autentica&ccedil;&atilde;o.

Aplicar configura&ccedil;&otilde;es no arquivo _azure.properties_ da aplica&ccedil;&atilde;o.

Os seguintes dados s&atilde;o valores default utilizados em testes:

```
Azure OAuth redirect URIs: http://localhost:8080/oauth2/callback
```


## Observa&ccedil;&otilde;es

As op&ccedil;&otilde;es de autentica&ccedil;&atilde;o externa devem ser habilitadas via context.xml:

```
  <!--
    *** Federated Authentication Flags ***

    Uma vez habilitada a flag, o framework irá buscar na aplicação pelo respectivo
    arquivo de properties para gerenciar a autenticação com o agente externo:

    - enableFacebookAuthentication : facebook.properties
    - enableAzureAuthentication    : azure.properties
    - enableGoogleAuthentication   : google.properties

  -->
  
  <Parameter name="enableFacebookAuthentication" value="false"/>
  <Parameter name="enableAzureAuthentication"    value="false"/>
  <Parameter name="enableGoogleAuthentication"   value="true"/>
```

Os valores destas 'flags' s&atilde;o carregados na classe _techne.Globals_ e podem ser utilizados, para, por exemplo, tornar din&acirc;mica a exibi&ccedil;&atilde;o ou n&atilde;o de um bot&atilde;o na tela de login:

```
boolean: Globals.FACEBOOK_AUTH_ENABLED
boolean: Globals.AZURE_AUTH_ENABLED
boolean: Globals.GOOGLE_AUTH_ENABLED
```


## Refer&ecirc;ncias

- [Jersey User Guide](https://jersey.java.net/documentation/latest/user-guide.html)

- [Google Identity Plataform](https://developers.google.com/identity/protocols/OpenIDConnect#accessingtheservice)