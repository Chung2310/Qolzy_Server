package com.example.Qolzy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
        List<Project> androidProjects = new ArrayList<>();
        androidProjects.add(new Project(
                "Qolzy",
                "https://github.com/Chung2310/Qolzy_Android",
                "Mạng xã hội chia sẻ bài viết, hình ảnh và video. Android + Spring Boot + JWT."
        ));
        androidProjects.add(new Project(
                "BeautyApp",
                "https://github.com/Chung2310/BeautyApp",
                "Ứng dụng tư vấn làm đẹp, đặt lịch với chuyên gia, Firebase Auth + Retrofit API."
        ));
        androidProjects.add(new Project(
                "Ứng dụng thương mại điện tử",
                "https://github.com/Chung2310/App_BanHang",
                "Ứng dụng bán hàng di động, giỏ hàng, đặt hàng, tích hợp API PHP."
        ));
        androidProjects.add(new Project(
                "Ứng dụng Android Điều khiển và hiển thị kết quả phân loại cà chua",
                "https://github.com/Chung2310/APPDoAnNhung",
                "Ứng dụng IoT điều khiển thiết bị và hiển thị phân loại cà chua qua AI."
        ));
        androidProjects.add(new Project(
                "Calculator",
                "https://github.com/Chung2310/Calculator",
                "Ứng dụng máy tính cơ bản trên Android."
        ));
        model.addAttribute("androidProjects", androidProjects);

// Dự án Back-end
        List<Project> backendProjects = new ArrayList<>();
        backendProjects.add(new Project(
                "Qolzy (Server)",
                "https://github.com/Chung2310/Qolzy_Server",
                "Back-end Spring Boot cho ứng dụng mạng xã hội Qolzy. Hỗ trợ JWT, MySQL, upload ảnh/video."
        ));
        backendProjects.add(new Project(
                "Ứng dụng thương mại điện tử (Server)",
                "https://github.com/Chung2310/shop_test",
                "Back-end cho app bán hàng, quản lý sản phẩm, đơn hàng, người dùng, API RESTful."
        ));
        backendProjects.add(new Project(
                "Ứng dụng Todo List",
                "https://github.com/Chung2310/Todolist_java_spring",
                "API quản lý công việc (CRUD) với Spring Boot + MySQL."
        ));
        model.addAttribute("backendProjects", backendProjects);


        // File download (giữ nguyên)
        model.addAttribute("files", new String[]{
                "cv.pdf",
                "demo.zip"
        });

        return "index";
    }
    public class Project {
        private String name;
        private String url;
        private String description;

        public Project(String name, String url, String description) {
            this.name = name;
            this.url = url;
            this.description = description;
        }

        // Getter
        public String getName() { return name; }
        public String getUrl() { return url; }
        public String getDescription() { return description; }
    }

}
