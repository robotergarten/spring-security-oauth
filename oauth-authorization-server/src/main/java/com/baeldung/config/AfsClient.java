package com.baeldung.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@EnableOAuth2Client
@Configuration
public class AfsClient {

    @Value("${oauth.resource:http://localhost:8082}")
    private String baseUrl;
    @Value("${oauth.authorize:http://localhost:8082/oauth/authorize}")
    private String authorizeUrl;
    @Value("${oauth.token:http://localhost:8082/oauth/token}")
    private String tokenUrl;


    @Bean
    protected OAuth2ProtectedResourceDetails resource() {

        ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();

        List scopes = new ArrayList<String>(2);
        scopes.add("write");
        scopes.add("read");
        resource.setAccessTokenUri(tokenUrl);
        resource.setClientId("restapp");
        resource.setClientSecret("restapp");
        resource.setGrantType("password");
        resource.setScope(scopes);

        resource.setUsername("**USERNAME**");
        resource.setPassword("**PASSWORD**");

        return resource;
    }

    @Bean
    public OAuth2RestOperations restTemplate() {
        AccessTokenRequest atr = new DefaultAccessTokenRequest();

        return new OAuth2RestTemplate(resource(), new DefaultOAuth2ClientContext(atr));
    }

}

@Service
@SuppressWarnings("unchecked")
class MyService {
    @Autowired
    private OAuth2RestOperations restTemplate;

    public MyService() {

        restTemplate.getAccessToken();
    }
}
