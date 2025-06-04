package com.example.mybackend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public interface PageController {
    @GetMapping("/international")
    String getInternationalPage();
}
