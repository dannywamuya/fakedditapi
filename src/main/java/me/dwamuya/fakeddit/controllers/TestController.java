package me.dwamuya.fakeddit.controllers;

import lombok.AllArgsConstructor;
import me.dwamuya.fakeddit.services.TestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TestController {

    private final TestService testService;

    @GetMapping("/")
    public String home() {
        return "<h1>Fakeddit</h1>";
    }

    @GetMapping("/admin")
    public String admin() {
        return "<h1>Welcome to the Admin Page</h1>";
    }

    @GetMapping("/hello")
    public String hello() {
        return "<h1>Hello World</h1>";
    }

    @GetMapping("/api/users")
    public ResponseEntity<?> users() {
        return ResponseEntity.ok().body(testService.getUsers());
    }

}