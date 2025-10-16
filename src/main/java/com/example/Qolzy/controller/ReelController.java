package com.example.Qolzy.controller;

import com.example.Qolzy.model.ApiResponse;
import com.example.Qolzy.model.reel.ReelResponse;
import com.example.Qolzy.service.ReelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/api/reels")
public class ReelController {
    @Autowired
    private ReelService reelService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReelResponse>>> getReels(@RequestParam Long userId,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        return  reelService.getReels(userId, page, size);
    }
}
