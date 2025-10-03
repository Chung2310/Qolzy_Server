package com.example.Qolzy.controller;

import com.example.Qolzy.model.ApiResponse;
import com.example.Qolzy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/username")
    public ResponseEntity<ApiResponse<String>> saveUserName(@RequestParam("userId") Long userId, @RequestParam("userName") String userName) {
        return userService.saveUserName(userName, userId);
    }
}
