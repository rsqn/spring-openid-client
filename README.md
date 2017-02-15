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
    <version>1.0.0</version>
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

### Ensure this is in your spring config
```
    <context:annotation-config/>
    <context:component-scan base-package="tech.rsqn.springopenidclient" />
```

... aaaand it should work