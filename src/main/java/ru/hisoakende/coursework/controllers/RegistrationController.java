package ru.hisoakende.coursework.controllers;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.hisoakende.coursework.services.UserService;

@CrossOrigin
@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/register", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String registerUser(@RequestParam MultiValueMap<String, String> formData) {
//        System.out.println(formData.get("username").stream().findFirst().get());
//        System.out.println(formData.get("password").stream().findFirst().get());
        userService.createUser(
                formData.get("username").stream().findFirst().get(),
                formData.get("password").stream().findFirst().get()
        );
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }
}
