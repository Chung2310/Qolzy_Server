package com.example.Qolzy.controller;

import com.example.Qolzy.model.ApiResponse;
import com.example.Qolzy.model.Messages;
import com.example.Qolzy.model.ResponseHandler;
import com.example.Qolzy.model.music.Music;
import com.example.Qolzy.model.post.Post;
import com.example.Qolzy.model.post.PostReponse;
import com.example.Qolzy.model.post.PostRequest;
import com.example.Qolzy.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostReponse>>> getPosts(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size,
                                                                   @RequestParam Long userId) {
        return postService.getPosts(page, size, userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<PostReponse>>> getPostsHistoryByUserId(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size,
                                                                   @PathVariable("userId") Long userId) {
        return postService.getPostsHistoryByUserId(page, size, userId);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<String>> createPost(@RequestParam(required = false) String content,
                                                          @RequestParam Long userId,
                                                          @RequestBody(required = false) Music music) throws IOException {
        return postService.createPost(content,userId,music);
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<String>> createPostFile(@RequestParam("postId") Long postId,
                                                              @RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException {
        return postService.createPostFile(postId ,files);
    }

    @PutMapping()
    public ResponseEntity<ApiResponse<String>> updatePost(@RequestBody PostRequest postRequest) {
        return postService.updatePost(postRequest);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> deletePost(@RequestParam Long postId) {
        return postService.deletePost(postId);
    }
}
