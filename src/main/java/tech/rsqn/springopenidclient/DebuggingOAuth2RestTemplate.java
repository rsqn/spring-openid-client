package tech.rsqn.springopenidclient;

import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import tech.rsqn.utils.LogRequestResponseFilter;

public class DebuggingOAuth2RestTemplate extends OAuth2RestTemplate {

    public DebuggingOAuth2RestTemplate(OAuth2ProtectedResourceDetails resource) {
        super(resource);
    }

    public DebuggingOAuth2RestTemplate(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {
        super(resource, context);
        getInterceptors().add(new LogRequestResponseFilter());
    }
}
