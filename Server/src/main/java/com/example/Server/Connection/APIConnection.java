package com.example.Server.Connection;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class APIConnection {
    static final String URL_GET_MESS = "http://localhost:8080/messages";
    static final String URL_GET_UMESS = "http://localhost:8080/messages/{username}";
    public static void PostMessage(String m)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestBody = new HttpEntity<>(m, headers);
        restTemplate.postForObject(URL_GET_MESS, requestBody, String.class);
    }

    public static String GetMessage(String m)
    {
        Map<String, String> params = new HashMap<>();
        params.put("username", m);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(URL_GET_UMESS, String.class, params);
    }

}
