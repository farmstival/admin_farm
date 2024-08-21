package com.joyfarm.farmstival.global.analytics.controllers;

import com.joyfarm.farmstival.global.analytics.service.GoogleAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/analytics")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class GoogleAnalyticsController {
    private final GoogleAnalyticsService googleAnalyticsService;

    @GetMapping("/dashboard")
    public String getDashboard() {
        return "analytics/visitorInfo";
    }

    @GetMapping("/visitorInfo")
    public ResponseEntity<Map<String, Object>> getVisitorInfo() {
        Map<String, Object> data = googleAnalyticsService.getWeeklyAndMonthlyData();
        return ResponseEntity.ok(data);
    }
}
