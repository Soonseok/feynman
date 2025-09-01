package dev.soon.feynman.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class HomeController {

    @GetMapping("/api/serverTime")
    public String home() {
        return "현재 서버 시간 : " + new Date();
    }
}
