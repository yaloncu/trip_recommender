package com.example.mybackend.controller.impl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mybackend.controller.PageController;

@RestController
public class PageControllerImpl implements PageController {
    @GetMapping("/international")
    public String getInternationalPage() {
        return "international";
    }
}
