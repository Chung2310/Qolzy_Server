package com.example.Qolzy.controller;

import com.example.Qolzy.model.ApiResponse;
import com.example.Qolzy.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/like")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @PutMapping()
    public ResponseEntity<ApiResponse<String>> createLike(@RequestParam String mode,@RequestParam Long userId,@RequestParam Long id) {
        return likeService.createLike(mode,id,userId);
    }
}
