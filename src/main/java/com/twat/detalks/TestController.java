package com.twat.detalks;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping
    public String testApi(){
        return "test api";
    }
}
