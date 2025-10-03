package com.example.Qolzy.controller;

import com.example.Qolzy.model.ApiResponse;
import com.example.Qolzy.model.follow.FollowResponse;
import com.example.Qolzy.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follow")
public class FollowController {
    @Autowired
    private FollowService followService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createFollow(Long followerId, Long followingId) {
        return followService.createFollow(followerId, followingId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<FollowResponse>>> getFollows(@PathVariable("userId") Long userId,
                                                                        @RequestParam int page,
                                                                        @RequestParam int size) {
        return followService.getFollows(userId, page, size);
    }
}
