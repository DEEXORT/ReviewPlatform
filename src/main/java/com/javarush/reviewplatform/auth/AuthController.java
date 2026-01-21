package com.javarush.reviewplatform.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final RegistrationService registrationService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
