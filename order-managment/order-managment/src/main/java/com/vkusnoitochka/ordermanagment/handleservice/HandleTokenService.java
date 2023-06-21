package com.vkusnoitochka.ordermanagment.handleservice;

import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class HandleTokenService {

    public String handleToken(@NotNull HttpServletRequest token) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token.getHeader("Authorization").substring(7));
        HttpEntity<?> entity = new HttpEntity<>(null, headers);
        URI userInfoUri = new URI("http://localhost:8080/api/user/get_info");
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                userInfoUri, HttpMethod.GET, entity, String.class);
        return  responseEntity.getBody();
    }
}
