package com.joyfarm.farmstival.main.controllers;

import com.joyfarm.farmstival.global.Utils;
import com.joyfarm.farmstival.global.analytics.service.GoogleAnalyticsService;
import com.joyfarm.farmstival.menus.Menu;
import com.joyfarm.farmstival.menus.MenuDetail;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MainController {
    private final Utils utils;
    private final GoogleAnalyticsService googleAnalyticsService;

    @ModelAttribute("menuCode")
    public String getMenuCode() { // 주 메뉴 코드
        return "main";
    }

    @ModelAttribute("subMenus")
    public List<MenuDetail> getSubMenus() { // 서브 메뉴
        return Menu.getMenus("main");
    }

    @GetMapping
    public String index(Model model, HttpServletRequest request) {
        return "main/index";
    }

    @GetMapping("/dashboard")
    public String getDashboard(Model model, HttpServletRequest request) {
        return "analytics/visitorInfo";
    }

    @GetMapping("/visitorInfo")
    public ResponseEntity<Map<String, Object>> getVisitorInfo() {
        Map<String, Object> data = googleAnalyticsService.getWeeklyAndMonthlyData();
        return ResponseEntity.ok(data);
    }
}
