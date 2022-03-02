package com.override.controller.rest;

import com.override.models.AuthRequest;
import com.override.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public String auth(@RequestBody AuthRequest request) {
        return authService.login(request.getLogin(), request.getPassword());
    }
}