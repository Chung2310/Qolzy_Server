package com.example.Qolzy.service;

import com.example.Qolzy.model.*;
import com.example.Qolzy.model.auth.UserEntity;
import com.example.Qolzy.model.comment.Comment;
import com.example.Qolzy.model.like.Like;
import com.example.Qolzy.model.post.Post;
import com.example.Qolzy.repository.CommentRepository;
import com.example.Qolzy.repository.LikeRepository;
import com.example.Qolzy.repository.PostRepository;
import com.example.Qolzy.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public Boolean getLikeByPostAndUserId(Long id, Long userId) {
        return likeRepository.existsByPostIdAndUserId(id, userId);
    }

    public Boolean getLikeByCommentIdAndUserId(Long commentId, Long userId) {
        return likeRepository.existsByCommentIdAndUserId(commentId, userId);
    }

    public ResponseEntity<ApiResponse<String>> createLike(String mode, Long id, Long userId) {
        log.info("[createLike] User {} like/unlike {} {}", userId, mode, id);

        if (id == null || userId == null || mode == null) {
            log.warn("[createLike] Thiếu id hoặc userId hoặc mode");
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO, HttpStatus.BAD_REQUEST, null);
        }

        // Kiểm tra user
        UserEntity userEntity = userRepository.findUserById(userId);
        if (userEntity == null) {
            log.error("[createLike] Không tìm thấy user {}", userId);
            return ResponseHandler.generateResponse(Messages.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);
        }

        if (mode.equalsIgnoreCase("post")) {
            Post post = postRepository.findById(id).orElse(null);
            if (post == null) {
                log.error("[likePost] Không tìm thấy post {}", id);
                return ResponseHandler.generateResponse(Messages.POST_NOT_FOUND, HttpStatus.NOT_FOUND, null);
            }

            if (likeRepository.existsByPostIdAndUserId(id, userId)) {
                // unlike
                likeRepository.deleteByPostIdAndUserId(id, userId);
                post.setLikes(post.getLikes() - 1);
                postRepository.save(post);

                log.info("[likePost] User {} đã unlike post {}", userId, id);
                return ResponseHandler.generateResponse(Messages.UNLIKE_CREATED, HttpStatus.OK, null);
            } else {
                // like
                Like like = new Like();
                like.setPost(post);
                like.setUser(userEntity);
                likeRepository.save(like);

                post.setLikes(post.getLikes() + 1);
                postRepository.save(post);

                log.info("[likePost] User {} đã like post {}", userId, id);
                return ResponseHandler.generateResponse(Messages.LIKE_CREATED, HttpStatus.CREATED, null);
            }

        } else if (mode.equalsIgnoreCase("comment")) {
            Comment comment = commentRepository.findById(id).orElse(null);
            if (comment == null) {
                log.error("[likeComment] Không tìm thấy comment {}", id);
                return ResponseHandler.generateResponse(Messages.COMMENT_NOT_FOUND, HttpStatus.NOT_FOUND, null);
            }

            if (likeRepository.existsByCommentIdAndUserId(id, userId)) {
                // unlike
                likeRepository.deleteByCommentIdAndUserId(id, userId);
                comment.setLikes(comment.getLikes() - 1);
                commentRepository.save(comment);

                log.info("[likeComment] User {} đã unlike comment {}", userId, id);
                return ResponseHandler.generateResponse(Messages.UNLIKE_COMMENT, HttpStatus.OK, null);
            } else {
                // like
                Like like = new Like();
                like.setComment(comment);
                like.setUser(userEntity);
                likeRepository.save(like);

                comment.setLikes(comment.getLikes() + 1);
                commentRepository.save(comment);

                log.info("[likeComment] User {} đã like comment {}", userId, id);
                return ResponseHandler.generateResponse(Messages.LIKE_COMMENT, HttpStatus.CREATED, null);
            }
        }

        return ResponseHandler.generateResponse("Invalid mode", HttpStatus.BAD_REQUEST, null);
    }



}
