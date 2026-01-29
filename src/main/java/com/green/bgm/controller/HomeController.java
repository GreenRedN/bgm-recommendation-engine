package com.green.bgm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        // 정적 페이지를 바로 열 수 있게 루트로 연결
        return "redirect:/bgm.html";
    }
}
