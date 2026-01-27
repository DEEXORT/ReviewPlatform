package com.javarush.reviewplatform.auth;

import com.javarush.reviewplatform.user.UserService;
import com.javarush.reviewplatform.user.UserTo;
import com.javarush.reviewplatform.util.Constant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RegistrationMvcController {
    private final UserService userService;

    @GetMapping(Constant.Path.REGISTER)
    public String register(Model model) {
        model.addAttribute("userTo", new UserTo());
        model.addAttribute("templateName", "register");
        model.addAttribute("fragmentName", "register-form");
        return Constant.View.MAIN;
    }

    @PostMapping(Constant.Path.REGISTER)
    public String register(@Valid @ModelAttribute("userTo") UserTo userTo,
                           BindingResult result, Model model) {
        model.addAttribute("templateName", "register");
        model.addAttribute("fragmentName", "register-form");

        if (userTo.getPassword() != null && !userTo.isPasswordMatch()) {
            result.rejectValue("password", "error.userTo", "Пароли не совпадают");
        }

        if (result.hasErrors()) {
            return Constant.View.MAIN;
        }

        if (userService.existsByEmail(userTo.getEmail())) {
            result.rejectValue("email", "error.userTo", "Пользователь с таким email уже существует");
        }

        userService.save(userTo);
        return "redirect:" + Constant.Path.PRODUCTS;
    }
}
