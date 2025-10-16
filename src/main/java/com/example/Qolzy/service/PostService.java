package com.example.Qolzy.service;

import com.example.Qolzy.mapper.PostMapper;
import com.example.Qolzy.model.*;
import com.example.Qolzy.model.auth.UserEntity;
import com.example.Qolzy.model.music.Music;
import com.example.Qolzy.model.post.*;
import com.example.Qolzy.repository.MusicRepository;
import com.example.Qolzy.repository.PostMediaRepository;
import com.example.Qolzy.repository.PostRepository;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostMediaRepository postMediaRepository;

    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ReelService reelService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public Post updatePost(Post post) {
        return postRepository.save(post);
    }

    public ResponseEntity<ApiResponse<String>> createPost(CreatePostRequest createPostRequest) {
        String content = createPostRequest.getContent();
        Long userId = createPostRequest.getUserId();
        Music music = createPostRequest.getMusic();
        log.info("Bắt đầu tạo post cho userId: {}", userId);

        // Kiểm tra dữ liệu đầu vào
        if (userId == null) {
            log.warn("Thiếu thông tin bắt buộc: content={}, userId={}", content, userId);
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO, HttpStatus.BAD_REQUEST, null);
        }

        UserEntity user = userService.findUserByUserId(userId);
        if (user == null) {
            log.error("Không tìm thấy user với id={}", userId);
            return ResponseHandler.generateResponse("User không tồn tại", HttpStatus.BAD_REQUEST, null);
        }

        Music musicToSet = null; // sẽ set vào post
        if (music != null && music.getId() != null) {
            Music existingMusic = musicRepository.findMusicById(music.getId());
            if (existingMusic == null) {
                // Lưu music mới nếu chưa có
                musicToSet = musicRepository.save(music);
            } else {
                musicToSet = existingMusic;
            }
        }

        // Tạo Post
        Post post = new Post();
        post.setContent(content);
        post.setUser(user);
        post.setMusic(musicToSet); // có thể null
        post = updatePost(post);

        user.setPostCount(user.getPostCount() + 1);
        userService.saveUser(user);

        log.info("Đã lưu post với id={}", post.getId());
        log.info("Tạo post thành công cho userId={}, postId={}", userId, post.getId());
        return ResponseHandler.generateResponse(Messages.POST_CREATED, HttpStatus.CREATED, post.getId().toString());
    }


    public ResponseEntity<ApiResponse<String>> createPostFile(Long postId,List<MultipartFile> files) throws IOException {
        if(postId == null && files == null) {
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO, HttpStatus.BAD_REQUEST, null);
        }

        Post post = postRepository.findById(postId).orElse(null);

        if(post == null){
            return ResponseHandler.generateResponse(Messages.POST_NOT_FOUND, HttpStatus.BAD_REQUEST, null);
        }

        // Xử lý upload file (nếu có)
        if (files != null && !files.isEmpty()) {
            log.info("Bắt đầu xử lý {} file upload", files.size());
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    log.warn("Bỏ qua file rỗng");
                    continue;
                }

                String fileName = mediaService.saveFile("post_media", file);
                String contentType = file.getContentType();

                // Xác định loại media
                PostMedia.MediaType mediaType = (contentType != null && contentType.startsWith("video"))
                        ? PostMedia.MediaType.VIDEO
                        : PostMedia.MediaType.IMAGE;

                PostMedia postMedia = new PostMedia();
                postMedia.setUrl(fileName);
                postMedia.setType(mediaType);
                postMedia.setPost(post);
                postMediaRepository.save(postMedia);

                if(postMedia.getType() == PostMedia.MediaType.VIDEO){
                    reelService.createReels(postId, fileName);
                }

                log.info("Đã lưu file '{}' loại={} cho postId={}", fileName, mediaType, post.getId());
            }
        } else {
            log.info("Không có file đính kèm");
        }

        return ResponseHandler.generateResponse(Messages.POST_CREATED, HttpStatus.CREATED, null);
    }

    public ResponseEntity<ApiResponse<List<PostReponse>>> getPosts(int page, int size, Long userId) {
        log.info("[getPosts] Đang lấy danh sách bài viết");

        Pageable pageable =  PageRequest.of(page, size);
        Page<Post> postPage = postRepository.findByIsDeletedFalse(pageable);

        List<PostReponse> postReponseList = postMapper.toPostReponseList(postPage.getContent());

        for (PostReponse postReponse: postReponseList){
            postReponse.setLikedByCurrentUser(likeService.getLikeByPostAndUserId(postReponse.getId(), userId));
            postReponse.setFollowByCurrentUser(followService.getFollowByFollowedAndFollowing(userId,postReponse.getUser().getId()));
        }
        log.debug("[getPosts] Số lượng bài viết lấy được lấy được: {}", postReponseList.size());

        return ResponseHandler.generateResponse(Messages.DATA_FETCH_SUCCESS, HttpStatus.OK, postReponseList);
    }

    public ResponseEntity<ApiResponse<List<PostReponse>>> getPostsHistoryByUserId(int page, int size, Long userId) {
        log.info("[getPostsHistoryByUserId] Đang lấy danh sách bài viết của người dùng có id :{}", userId);

        Pageable pageable =  PageRequest.of(page, size);
        Page<Post> postPage = postRepository.findByIsDeletedFalseAndUserId(userId,pageable);


        List<PostReponse> postReponseList = postMapper.toPostReponseList(postPage.getContent());

        for (PostReponse postReponse: postReponseList){
            postReponse.setLikedByCurrentUser(likeService.getLikeByPostAndUserId(postReponse.getId(), userId));
        }
        log.debug("[getPosts] Số lượng bài viết lấy được lấy được: {}", postReponseList.size());

        return ResponseHandler.generateResponse(Messages.DATA_FETCH_SUCCESS, HttpStatus.OK, postReponseList);
    }

    public ResponseEntity<ApiResponse<String>> updatePost(PostRequest postRequest) {
        log.info("[updatePost] Yêu cầu cập nhật bài viết: {}", postRequest);

        String content = postRequest.getContent();
        Long postId = postRequest.getId();

        if (content == null || postId == null) {
            log.warn("[updatePost] Thiếu thông tin bắt buộc: content={}, postId={}", content, postId);
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO, HttpStatus.BAD_REQUEST, null);
        }

        Post post = postRepository.findById(postId).orElse(null);

        if (post == null) {
            log.error("[updatePost] Không tìm thấy bài viết với id={}", postId);
            return ResponseHandler.generateResponse(Messages.POST_NOT_FOUND, HttpStatus.NOT_FOUND, null);
        }

        log.debug("[updatePost] Nội dung cũ: {}", post.getContent());
        post.setContent(content);
        post = updatePost(post);

        log.info("[updatePost] Cập nhật thành công postId={}, nội dung mới={}", post.getId(), post.getContent());

        return ResponseHandler.generateResponse(Messages.POST_UPDATED, HttpStatus.OK, null);
    }

    public ResponseEntity<ApiResponse<String>> deletePost(Long postId) {
        log.info("[deletePost] Yêu cầu xóa bài viết với postId={}", postId);

        if (postId == null) {
            log.warn("[deletePost] Thiếu postId trong request");
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO, HttpStatus.BAD_REQUEST, null);
        }

        Post post = postRepository.findById(postId).orElse(null);

        if (post == null) {
            log.error("[deletePost] Không tìm thấy bài viết với id={}", postId);
            return ResponseHandler.generateResponse(Messages.POST_NOT_FOUND, HttpStatus.NOT_FOUND, null);
        }

        post.setDeleted(true);
        updatePost(post);

        log.info("[deletePost] Xóa thành công bài viết với id={}", postId);
        return ResponseHandler.generateResponse(Messages.POST_DELETED, HttpStatus.OK, null);
    }


}
