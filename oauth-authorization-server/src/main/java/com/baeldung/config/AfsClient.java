package com.baeldung.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

        ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();

        List scopes = new ArrayList<String>(2);
        //scopes.add("test");
        resource.setAccessTokenUri(tokenUrl);
        resource.setClientId("test_poc");
        resource.setClientSecret("secret");
        resource.setGrantType("client_credentials");
        //resource.setScope(scopes);
        resource.setClientAuthenticationScheme(AuthenticationScheme.header);

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
class AfsService {
    private static String clientLstUri = "https://example/v1/auth/clients";

    @Autowired
    private OAuth2RestOperations restTemplate;

    public OAuth2AccessToken getAfsToken() {
        return restTemplate.getAccessToken();
    }

    public ResponseEntity<String> getAfsClientDetails(OAuth2AccessToken token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token.getValue());
        //headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        return restTemplate.exchange(clientLstUri, HttpMethod.GET, entity, String.class);

    }
}
