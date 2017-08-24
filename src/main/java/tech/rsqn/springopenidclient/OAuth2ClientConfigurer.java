package tech.rsqn.springopenidclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import tech.rsqn.utils.LogRequestResponseFilter;

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

    @Value("${oauth2.enableDebugging}")
    private boolean enableDebugging;

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

        if (enableDebugging) {
            List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
            interceptors.add(new LogRequestResponseFilter());

            AuthorizationCodeAccessTokenProvider a = new AuthorizationCodeAccessTokenProvider();
            ImplicitAccessTokenProvider b = new ImplicitAccessTokenProvider();
            ResourceOwnerPasswordAccessTokenProvider c = new ResourceOwnerPasswordAccessTokenProvider();
            ClientCredentialsAccessTokenProvider d = new ClientCredentialsAccessTokenProvider();

            a.setInterceptors(interceptors);
            b.setInterceptors(interceptors);
            c.setInterceptors(interceptors);
            d.setInterceptors(interceptors);

            AccessTokenProviderChain accessTokenProvider = new AccessTokenProviderChain(Arrays.<AccessTokenProvider>asList(
                    a, b, c, d));

            DebuggingOAuth2RestTemplate ret = new DebuggingOAuth2RestTemplate(getAuth2ClientDetails(), oAuth2ClientContext);
            ret.setAccessTokenProvider(accessTokenProvider);
            return ret;
        } else {
            return new OAuth2RestTemplate(getAuth2ClientDetails(), oAuth2ClientContext);
        }
    }
}
