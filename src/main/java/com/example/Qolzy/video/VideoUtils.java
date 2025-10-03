package com.example.Qolzy.video;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class VideoUtils {

    // Kiểm tra có phải video không
    public static boolean isVideo(MultipartFile file) {
        if (file == null || file.isEmpty()) return false;

        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("video");
    }

    // Lấy độ dài video (ms)
    public static long getVideoDurationMillis(MultipartFile file) throws IOException {
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(file.getInputStream())) {
            grabber.start();
            long duration = grabber.getLengthInTime() / 1000; // microseconds -> milliseconds
            grabber.stop();
            return duration;
        } catch (Exception e) {
            throw new IOException("Không thể đọc file video", e);
        }
    }

    // Validate video (chỉ nhận video <= 20s)
    public static void validateVideo(MultipartFile file) throws IOException {
        if (!isVideo(file)) {
            throw new IOException("File phải là video!");
        }

        long durationMillis = getVideoDurationMillis(file);
        if (durationMillis > 20_000) { // > 20s
            throw new IOException("Video không được dài quá 20 giây!");
        }
    }
}
