package com.example.Qolzy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class PortfolioController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("name", "Đỗ Đức Chung");
        model.addAttribute("email", "chunga00d@gmail.com");
        model.addAttribute("facebook", "https://www.facebook.com/oucchung.602301/");
        model.addAttribute("github", "https://github.com/Chung2310");


        // Dự án Android
        Map<String, String> androidProjects = new LinkedHashMap<>();
        androidProjects.put("Qolzy", "https://github.com/username/android-app1");
        androidProjects.put("App 2", "https://github.com/username/android-app2");
        model.addAttribute("androidProjects", androidProjects);

        // Dự án Back-end
        Map<String, String> backendProjects = new LinkedHashMap<>();
        backendProjects.put("Qolzy", "https://github.com/username/backend-api1");
        backendProjects.put("Ứng dụng thương mại điện tử", "https://github.com/Chung2310/shop_test");
        model.addAttribute("backendProjects", backendProjects);

        // File download (giữ nguyên)
        model.addAttribute("files", new String[]{
                "cv.pdf",
                "demo.zip"
        });

        return "index";
    }
}
