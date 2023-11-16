package com.example.JAVAProjectPintilieVictor.Controllers;

import com.example.JAVAProjectPintilieVictor.Entities.User;
import com.example.JAVAProjectPintilieVictor.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PasswordResetController {
    @Autowired
    private UserService userService;

    @GetMapping("/password-reset")
    public String showPasswordResetForm(Model model) {
        model.addAttribute("user", new User());
        return "password-reset";
    }

    @PostMapping("/password-reset")
    public String resetPassword(@ModelAttribute("user") User user, @RequestParam("newPassword") String newPassword) {
        userService.resetUserPassword(user, newPassword);
        return "redirect:/login";
    }
}

