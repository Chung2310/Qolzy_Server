package com.example.Qolzy.controller;

import com.example.Qolzy.model.ApiResponse;
import com.example.Qolzy.model.comment.CommentRepliesResponse;
import com.example.Qolzy.model.comment.CommentRequest;
import com.example.Qolzy.model.comment.CommentResponse;
import com.example.Qolzy.model.comment.CommentUpdateRequest;
import com.example.Qolzy.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentsByPost(@PathVariable Long postId,
                                                                                @RequestParam Long userId,
                                                                                @RequestParam(defaultValue = "0") int page,
                                                                                @RequestParam(defaultValue = "5") int  size) {
        return commentService.getCommentsByPost(postId, userId,page, size);
    }

    @GetMapping("/replies/{commentId}")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getRepliesByPost(@PathVariable Long commentId,
                                                                                      @RequestParam Long userId,
                                                                                      @RequestParam(defaultValue = "0") int page,
                                                                                      @RequestParam(defaultValue = "5") int size) {
        return commentService.getRepliesByComment(commentId,userId ,page, size);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createComment(@RequestBody CommentRequest commentRequest) {
        return commentService.createComment(commentRequest);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<String>> updateComment(@RequestBody CommentUpdateRequest commentUpdateRequest) {
        return commentService.updateComment(commentUpdateRequest);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> deleteComment(@RequestParam Long commentId) {
        return commentService.deleteComment(commentId);
    }
}

