package com.joyfarm.farmstival.global.analytics.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoogleAnalyticsService {

    private final RestTemplate restTemplate;

    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.client-secret}")
    private String clientSecret;

    @Value("${google.refresh-token}")
    private String refreshToken;

    @Value("${google.token-uri}")
    private String tokenUri;

    @Value("${google.property-id}")
    private String propertyId;

    public GoogleAnalyticsService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    // Access Token 발급
    public String getAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("refresh_token", refreshToken);
        params.add("grant_type", "refresh_token");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUri, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody().get("access_token").toString();
        } else {
            throw new RuntimeException("Failed to get access token");
        }
    }

    public Map<String, Object> getWeeklyAndMonthlyData() {
        String accessToken = getAccessToken();

        // GA4 API 요청 URL
        String url = "https://analyticsdata.googleapis.com/v1beta/properties/" + propertyId + ":runReport";

        // HTTP Headers 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 일주일치 데이터 요청
        Map<String, Object> weeklyRequestBody = new HashMap<>();
        weeklyRequestBody.put("dateRanges", List.of(Map.of("startDate", "2024-08-14", "endDate", "today"))); // 일주일 전부터 오늘까지
        weeklyRequestBody.put("metrics", List.of(Map.of("name", "activeUsers")));
        weeklyRequestBody.put("dimensions", List.of(Map.of("name", "date")));

        HttpEntity<Map<String, Object>> weeklyEntity = new HttpEntity<>(weeklyRequestBody, headers);

        // 한 달치 데이터 요청
        Map<String, Object> monthlyRequestBody = new HashMap<>();
        monthlyRequestBody.put("dateRanges", List.of(Map.of("startDate", "2024-07-21", "endDate", "today"))); // 한 달 전부터 오늘까지
        monthlyRequestBody.put("metrics", List.of(Map.of("name", "activeUsers")));
        monthlyRequestBody.put("dimensions", List.of(Map.of("name", "date")));

        HttpEntity<Map<String, Object>> monthlyEntity = new HttpEntity<>(monthlyRequestBody, headers);

        // API 호출
        ResponseEntity<Map> weeklyResponse = restTemplate.postForEntity(url, weeklyEntity, Map.class);
        ResponseEntity<Map> monthlyResponse = restTemplate.postForEntity(url, monthlyEntity, Map.class);

        Map<String, Object> result = new HashMap<>();
        if (weeklyResponse.getStatusCode() == HttpStatus.OK && weeklyResponse.getBody() != null) {
            result.put("weeklyData", weeklyResponse.getBody());
        } else {
            throw new RuntimeException("Failed to retrieve weekly data");
        }

        if (monthlyResponse.getStatusCode() == HttpStatus.OK && monthlyResponse.getBody() != null) {
            result.put("monthlyData", monthlyResponse.getBody());
        } else {
            throw new RuntimeException("Failed to retrieve monthly data");
        }

        return result;
    }


}