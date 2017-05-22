package tech.rsqn.springopenidclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.oauth2.common.AuthenticationScheme.form;


@Configuration
@EnableOAuth2Client
public class OAuth2ClientConfigurer {

    @Value("${oauth2.authUri}")
    private String authUri;

    @Value("${oauth2.accessTokenUri}")
    private String accessTokenUri;

    @Value("${oauth2.clientId}")
    private String clientId;

    @Value("${oauth2.clientSecret}")
    private String clientSecret;

    @Value("${oauth2.scope}")
    private String scope;

    @Value("${oauth2.authenticationScheme}")
    private String authenticationScheme;

    @Bean
    public OAuth2ProtectedResourceDetails getAuth2ClientDetails() {
        AuthorizationCodeResourceDetails oauth2ClientDetails = new AuthorizationCodeResourceDetails();
        oauth2ClientDetails.setAuthenticationScheme(form);
        oauth2ClientDetails.setClientAuthenticationScheme(form);
        oauth2ClientDetails.setClientId(clientId);
        oauth2ClientDetails.setClientSecret(clientSecret);
        oauth2ClientDetails.setUserAuthorizationUri(authUri);
        oauth2ClientDetails.setAccessTokenUri(accessTokenUri);
        oauth2ClientDetails.setScope(asList(scope));
        oauth2ClientDetails.setAuthenticationScheme(AuthenticationScheme.valueOf(authenticationScheme));
        return oauth2ClientDetails;
    }

    private List<String> asList(String s) {
        List<String> l = new ArrayList<String>(Arrays.asList(s.split(",")));
        return l;
    }

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Resource
    private OAuth2ClientContext oAuth2ClientContext;

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
    public OAuth2RestOperations oAuth2RestTemplate() {
        return new OAuth2RestTemplate(getAuth2ClientDetails(), oAuth2ClientContext);
    }
}
