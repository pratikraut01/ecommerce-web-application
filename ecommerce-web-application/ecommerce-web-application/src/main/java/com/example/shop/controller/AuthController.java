package com.example.shop.controller;

import com.example.shop.model.User;
import com.example.shop.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final UserRepository users;
    private final BCryptPasswordEncoder enc;

    public AuthController(UserRepository users, BCryptPasswordEncoder enc) {
        this.users = users;
        this.enc = enc;
    }

    @GetMapping("/login")
    public String loginPage() { return "login"; }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password,
                        HttpSession session, Model model) {
        User u = users.findByEmail(email.trim().toLowerCase()).orElse(null);
        if (u == null || !enc.matches(password, u.getPasswordHash())) {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
        session.setAttribute("uid", u.getId());
        session.setAttribute("uname", u.getName());
        session.setAttribute("role", u.getRole());
        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerPage() { return "register"; }

    @PostMapping("/register")
    public String register(@RequestParam String name, @RequestParam String email,
                           @RequestParam String password, Model model) {
        String mail = email.trim().toLowerCase();
        if (name.isBlank() || password.length() < 6) {
            model.addAttribute("error", "Name is required and password needs at least 6 characters");
            return "register";
        }
        if (users.existsByEmail(mail)) {
            model.addAttribute("error", "Email already registered");
            return "register";
        }
        User u = new User();
        u.setName(name.trim()); u.setEmail(mail); u.setPasswordHash(enc.encode(password));
        users.save(u);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
