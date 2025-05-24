package com.ArnabMdev.ArkaneGames.controller;

import com.ArnabMdev.ArkaneGames.entity.User;
import com.ArnabMdev.ArkaneGames.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() &&
            !authentication.getPrincipal().equals("anonymousUser")) {

            if (authentication.getPrincipal() instanceof OAuth2User) {
                OAuth2User principal = (OAuth2User) authentication.getPrincipal();
                model.addAttribute("name", principal.getAttribute("name"));
                model.addAttribute("email", principal.getAttribute("email"));
                model.addAttribute("picture", principal.getAttribute("picture"));
            } else if (authentication.getPrincipal() instanceof UserDetails) {
                String email = ((UserDetails) authentication.getPrincipal()).getUsername();
                Optional<User> userOpt = userRepository.findByEmail(email);

                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    model.addAttribute("name", user.getName());
                    model.addAttribute("email", user.getEmail());
                    model.addAttribute("picture", user.getPictureUrl());
                }
            }
        }

        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof OAuth2User) {
                OAuth2User principal = (OAuth2User) authentication.getPrincipal();
                model.addAttribute("name", principal.getAttribute("name"));
                model.addAttribute("email", principal.getAttribute("email"));
                model.addAttribute("picture", principal.getAttribute("picture"));

                // For OAuth users, get the provider from the authorities
                String authProvider = "OAuth2";
                if (principal.getAttributes().containsKey("iss")) {
                    // For Google
                    authProvider = "Google";
                } else if (principal.getAttribute("login") != null) {
                    // For GitHub
                    authProvider = "GitHub";
                }

                model.addAttribute("authProvider", authProvider);
            } else if (authentication.getPrincipal() instanceof UserDetails) {
                String email = ((UserDetails) authentication.getPrincipal()).getUsername();
                Optional<User> userOpt = userRepository.findByEmail(email);

                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    model.addAttribute("name", user.getName());
                    model.addAttribute("email", user.getEmail());
                    model.addAttribute("picture", user.getPictureUrl());
                    model.addAttribute("authProvider", "Local Account");
                }
            }
        }

        return "profile";
    }
}

