package com.example.Qolzy.service;

import com.example.Qolzy.mapper.StoryMapper;
import com.example.Qolzy.model.ApiResponse;
import com.example.Qolzy.model.Messages;
import com.example.Qolzy.model.ResponseHandler;
import com.example.Qolzy.model.auth.UserEntity;
import com.example.Qolzy.model.follow.Follow;
import com.example.Qolzy.model.post.Post;
import com.example.Qolzy.model.post.PostMedia;
import com.example.Qolzy.model.post.PostReponse;
import com.example.Qolzy.model.story.Story;
import com.example.Qolzy.model.story.StoryResponse;
import com.example.Qolzy.model.story.StoryView;
import com.example.Qolzy.repository.PostMediaRepository;
import com.example.Qolzy.repository.StoryRepository;
import com.example.Qolzy.repository.StoryViewRepository;
import com.example.Qolzy.video.VideoUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StoryService {
    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private StoryViewRepository storyViewRepository;

    @Autowired
    private PostMediaRepository postMediaRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private StoryMapper storyMapper;

    @Autowired
    private FollowService followService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public ResponseEntity<ApiResponse<List<StoryResponse>>> getStories(Long userId) throws IOException {
        log.info("[getStories] Đang lấy danh sách story");

        List<Follow> follows = followService.getFollowsByUserId(userId);

        List<Long> followerIds = new ArrayList<>();

        for (Follow follow : follows) {
            followerIds.add(follow.getFollowing().getId());
        }

        List<Story> stories = storyRepository.findByUserIdInAndExpiresAtAfter(followerIds, LocalDateTime.now());

        List<StoryResponse> storyResponseList = storyMapper.toStoryResponseList(stories);

        log.debug("[getStories] Số lượng story lấy được lấy được: {}", storyResponseList.size());

        return ResponseHandler.generateResponse(Messages.DATA_FETCH_SUCCESS, HttpStatus.OK, storyResponseList);
    }

    public ResponseEntity<ApiResponse<List<StoryResponse>>> getStoriesHistoryByUserId(Long userId, int page, int size) throws IOException {
        log.info("[getStoriesHistoryByUserId] Đang lấy danh sách lịch sử story cho người dùng {}", userId );

        Pageable pageable = PageRequest.of(page,size);


        Page<Story> stories = storyRepository.findByUserId(userId, pageable);

        List<StoryResponse> storyResponseList = storyMapper.toStoryResponseList(stories.getContent());

        log.debug("[getStoriesHistoryByUserId] Số lượng story lấy được lấy được: {}", storyResponseList.size());

        return ResponseHandler.generateResponse(Messages.DATA_FETCH_SUCCESS, HttpStatus.OK, storyResponseList);
    }

    public ResponseEntity<ApiResponse<String>> createViewStory(Long userid, Long storyId){
        if(userid == null || storyId == null){
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO, HttpStatus.BAD_REQUEST, null);
        }

        UserEntity user = userService.findUserByUserId(userid);

        if (user == null) {
            return ResponseHandler.generateResponse(Messages.USER_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
        }

        Story story = storyRepository.findStoryById(storyId);

        if (story == null) {
            return ResponseHandler.generateResponse(Messages.STORY_NOT_FOUND, HttpStatus.NOT_FOUND, null);
        }

        StoryView storyView = new StoryView();
        storyView.setStory(story);
        storyView.setViewer(user);
        storyViewRepository.save(storyView);

        return ResponseHandler.generateResponse(Messages.STORY_VIEW_CREATED, HttpStatus.CREATED, null);
    }

    public ResponseEntity<ApiResponse<String>> createStory(Long userId, MultipartFile file) throws IOException {
        log.info("Bắt đầu tạo story cho userId: {}", userId);

        // Kiểm tra dữ liệu đầu vào
        if (userId == null || file == null || file.isEmpty()) {
            log.warn("Thiếu thông tin bắt buộc: userId={} hoặc file rỗng", userId);
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO, HttpStatus.BAD_REQUEST, null);
        }

        // Kiểm tra user
        UserEntity user = userService.findUserByUserId(userId);
        if (user == null) {
            log.error("Không tìm thấy user với id={}", userId);
            return ResponseHandler.generateResponse("User không tồn tại", HttpStatus.BAD_REQUEST, null);
        }

        // ✅ Validate video
        try {
            VideoUtils.validateVideo(file);
        } catch (IOException e) {
            log.error("File upload không hợp lệ: {}", e.getMessage());
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }

        // Tạo Story
        Story story = new Story();
        story.setUser(user);
        story = storyRepository.save(story);

        log.info("Đã lưu story với id={}", story.getId());

        // Lưu file media
        String fileName = mediaService.saveFile("story_media", file);
        String contentType = file.getContentType();

        PostMedia.MediaType mediaType = (contentType != null && contentType.startsWith("video"))
                ? PostMedia.MediaType.VIDEO
                : PostMedia.MediaType.IMAGE; // thực tế chỉ cho VIDEO, IMAGE không bao giờ pass validate

        PostMedia postMedia = new PostMedia();
        postMedia.setUrl(fileName);
        postMedia.setType(mediaType);
        postMedia.setStory(story);
        postMediaRepository.save(postMedia);

        log.info("Đã lưu video '{}' cho storyId={}", fileName, story.getId());

        return ResponseHandler.generateResponse(Messages.STORY_CREATED, HttpStatus.CREATED, null);
    }

}
