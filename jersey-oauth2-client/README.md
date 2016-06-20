# Jersey OAuth2 Client

Based on [Jersey Security: OAuth2 Support](https://jersey.java.net/documentation/latest/user-guide.html#d0e12830) it handles authentication with the following identity providers: Facebook, Azure e Google.


### Facebook

Register your app > [Facebook Developers](https://developers.facebook.com/apps).

Example: 
```
Valid OAuth redirect URIs: http://localhost:8080/oauth2/callback
```


### Azure AD

Register your app > [Windows Azure AD](https://manage.windowsazure.com/<your domain>#Workspaces/ActiveDirectoryExtension/directory)

Example:
```
SIGN-ON URL: http://localhost:8080
APP ID URI:  http://localhost:8080
REPLY URL:   http://localhost:8080/oauth2/callback
USER ASSIGNMENT REQUIRED TO ACCESS APP: YES
```


### Google

Register your app > [Google Developers](https://console.developers.google.com)

Example:
```
Redirect URIs:       http://localhost:8080/oauth2/callback
JavaScript origins:  http://localhost:8080
```


## Configuration...


#### EndPoints

1. /oauth2/authorize
2. /oauth2/callback
3. /oauth2/revoke


## TO DOs:

1. [ ] Decouple from app implementation and make it self-contained;
	
2. [ ] Implement 'Require App Secret' on facebook;

3. [ ] Finish documentation;


## References

- [The Oauth 2 Authorization Framework](https://tools.ietf.org/html/rfc6749)

- [Overview sobre OpenID Connect](http://openid.net/connect/)

- [Jersey User Guide](https://jersey.java.net/documentation/latest/user-guide.html)

- [Facebook Manually Build a Login Flow](https://developers.facebook.com/docs/facebook-login/manually-build-a-login-flow)

- [Microsoft Azure AD: Authorization Code Grant Flow](https://msdn.microsoft.com/en-us/library/azure/dn645542.aspx)

- [Google Identity Plataform](https://developers.google.com/identity/protocols/OpenIDConnect#accessingtheservice)

