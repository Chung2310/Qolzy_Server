package com.example.Qolzy.service;

import com.example.Qolzy.model.*;
import com.example.Qolzy.model.auth.UserEntity;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    public ResponseEntity<ApiResponse<String>> uploadSingle(Long userId, String mode, MultipartFile file) {
        log.info("[uploadSingle] Bắt đầu upload file cho userId={}, mode={}", userId, mode);

        try {
            if (file.isEmpty()) {
                log.warn("[uploadSingle] File trống!");
                return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO, HttpStatus.BAD_REQUEST, null);
            }

            log.info("[uploadSingle] File nhận được: {}", file.getOriginalFilename());
            log.debug("[uploadSingle] Content-Type={}, Size={} bytes", file.getContentType(), file.getSize());

            String filePath = saveFile(mode, file);

            if (userId != null && "avatar".equals(mode)) {
                log.info("[uploadSingle] Đang cập nhật avatar cho userId={}", userId);
                UserEntity user = userService.findUserByUserId(userId);
                if (user == null) {
                    log.warn("[uploadSingle] Không tìm thấy userId={}", userId);
                    return ResponseHandler.generateResponse(Messages.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);
                }
                user.setAvatarUrl(filePath);
                userService.saveUser(user);
                log.info("[uploadSingle] Avatar đã được cập nhật thành công cho userId={}", userId);
            }

            log.info("[uploadSingle] Upload thành công, filePath={}", filePath);
            return ResponseHandler.generateResponse(Messages.IMAGE_UPLOAD_SUCCESS, HttpStatus.CREATED, filePath);
        } catch (Exception e) {
            log.error("[uploadSingle] Lỗi upload file: {}", e.getMessage(), e);
            return ResponseHandler.generateResponse(Messages.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    public String saveFile(String mode, MultipartFile file) throws IOException {
        log.info("[saveFile] Bắt đầu lưu file cho mode={}, originalName={}", mode, file.getOriginalFilename());

        Path uploadDir = Paths.get("uploads/" + mode);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
            log.info("[saveFile] Đã tạo thư mục: {}", uploadDir.toAbsolutePath());
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = uploadDir.resolve(fileName);

        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        log.info("[saveFile] Đã lưu file thành công tại: {}", path.toAbsolutePath());

        String relativePath = "/uploads/" + mode + "/" + fileName;
        log.info("[saveFile] Trả về relativePath={}", relativePath);
        return relativePath;
    }
}
