# spring-openid-client
An "off the shelf" implementation of spring as an oauth2 or openid connect client.

There is nothing special in here and this does nothing that spring does not do out of the box.

However there are so many examples "on the internet" nowdays and many of them make assumptions on technical stacks or don't provide a full working
solution that it can actually be difficult to get this going as a client only.

This module is simply to alleviate the need to learn something that really should be just a commodity


How do I use this?
==================

### Add this dependency
```
 <dependency>
    <groupId>tech.rsqn</groupId>
    <artifactId>spring-openid-client</artifactId>
    <version>1.0.3</version>
 </dependency>
```

### Add spring dependencies to your project as the dependencies in this project are scoped as "provided"
```
todo - have a look at the POM file
```

### Ensure these properties are available in your spring context
```
oauth2.authUri=XX
oauth2.accessTokenUri=XX
oauth2.userInfoUri=XX
oauth2.clientId=XX
oauth2.clientSecret=XX
oauth2.scope=trust,openid
oauth2.authenticationScheme=header
```

### Create a web security configuration class like this in your project
```
    
@Configuration
@EnableWebSecurity
public class OAuth2WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final String AUTH_URI = "/app_auth";

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint(AUTH_URI);
    }

    @Bean
    public OpenIDConnectAuthenticationFilter openIdConnectAuthenticationFilter() {
        return new OpenIDConnectAuthenticationFilter(AUTH_URI);
    }

    @Bean
    public OAuth2ClientContextFilter oAuth2ClientContextFilter() {
        return new OAuth2ClientContextFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(oAuth2ClientContextFilter(), AbstractPreAuthenticatedProcessingFilter.class)
                .addFilterAfter(openIdConnectAuthenticationFilter(), OAuth2ClientContextFilter.class)
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
                .and().authorizeRequests()
                .antMatchers(HttpMethod.GET, "/unprotected").permitAll()
                .antMatchers(HttpMethod.GET, "/*").authenticated();
    }
}

```


### Ensure this is in your spring config
```
    <context:annotation-config/>
    <context:component-scan base-package="tech.rsqn.springopenidclient" />
    <context:component-scan base-package="THE_PACKAGE_YOU_HAVE_THE_ABOVE_CONFIGURATION_CLASS_IN" />

```

... aaaand it should work