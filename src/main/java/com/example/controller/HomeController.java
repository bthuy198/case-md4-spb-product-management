package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("")
    public String showHomePage() {
        return "login";
    }
    @GetMapping("/products")
    private String showProduct() {
        return "product/product-list";
    }
}
