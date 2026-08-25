package io.github.lucascmc1.gymflow.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    String login() {
        return "login";
    }
}
