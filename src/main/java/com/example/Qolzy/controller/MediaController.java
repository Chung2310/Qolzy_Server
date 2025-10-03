package com.example.Qolzy.controller;

import com.example.Qolzy.model.ApiResponse;
import com.example.Qolzy.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @PostMapping()
    public ResponseEntity<ApiResponse<String>> uploadSingle(
            @RequestParam(required = false) Long userId,
            @RequestParam String mode,
            @RequestParam("file") MultipartFile file) {
        return mediaService.uploadSingle(userId, mode, file);
    }


}
