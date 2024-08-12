package com.joyfarm.farmstival.main.controllers;

import com.joyfarm.farmstival.global.Utils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {
    private final Utils utils;
    @GetMapping
    public String index(Model model, HttpServletRequest request) {
        return "main/index";
    }
}
