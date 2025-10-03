package com.example.Qolzy.controller;

import com.example.Qolzy.model.ApiResponse;
import com.example.Qolzy.model.story.StoryResponse;
import com.example.Qolzy.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/story")
public class StoryController {
    @Autowired
    private StoryService storyService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createStory(@RequestParam Long userId,
                                                           @RequestPart(value = "file") MultipartFile file) throws IOException {
        return storyService.createStory(userId,file);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StoryResponse>>> getStories(@RequestParam Long userId) throws IOException {
        return storyService.getStories(userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<StoryResponse>>> getStoriesHistoryByUserId(@PathVariable("userId") Long userId,
                                                                                      @RequestParam int page,
                                                                                      @RequestParam int size) throws IOException {
        return storyService.getStoriesHistoryByUserId(userId, page, size);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<String>> createViewStory(@RequestParam Long userId, @RequestParam Long storyId) throws IOException {
        return storyService.createViewStory(userId,storyId);
    }
}
