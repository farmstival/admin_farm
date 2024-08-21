package com.joyfarm.farmstival.global.analytics.service;

import org.springframework.beans.factory.annotation.Value;

public class GoogleAnalyticsService {

    @Value("${google.api.client-id}")
    private String clientId;

    @Value("${google.api.client-secret}")
    private String clientSecret;

    @Value("${google.api.refresh-token}")
    private String refreshToken;

    @Value("${google.analytics.property-id}")
    private String propertyId;


}