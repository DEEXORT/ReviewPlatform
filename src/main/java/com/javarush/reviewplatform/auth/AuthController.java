package com.javarush.reviewplatform.auth;

import com.javarush.reviewplatform.user.UserService;
import com.javarush.reviewplatform.util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping(Constant.Path.LOGIN)
    public String login() {
        return Constant.View.LOGIN;
    }

}
