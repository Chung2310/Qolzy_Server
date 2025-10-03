package com.example.Qolzy.service;

import com.example.Qolzy.mapper.CommentMapper;
import com.example.Qolzy.model.ApiResponse;
import com.example.Qolzy.model.Messages;
import com.example.Qolzy.model.ResponseHandler;
import com.example.Qolzy.model.auth.UserEntity;
import com.example.Qolzy.model.comment.*;
import com.example.Qolzy.model.post.Post;
import com.example.Qolzy.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private CommentMapper commentMapper;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentsByPost(Long postId, Long userId, int page, int size) {
        log.info("📥 Lấy danh sách bình luận cho bài viết có postId={} | trang={} | kích thước={}", postId, page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<Comment> replies = commentRepository.findByPostIdAndParentIsNullAndIsDeletedFalse(postId, pageable);

        if (replies.isEmpty()) {
            log.warn("⚠️ Không tìm thấy bình luận nào cho postId={}", postId);
        } else {
            log.debug("🔍 Tìm thấy {} bình luận cho postId={}", replies.getTotalElements(), postId);
        }
        List<Comment> comments = replies.getContent();

        List<CommentResponse> commentResponses = commentMapper.toCommentResponseList(comments);

        for (CommentResponse commentResponse: commentResponses){
            commentResponse.setCountComment(commentRepository.countByParentId(commentResponse.getId()));
            commentResponse.setParenId(0L);
            if (likeService.getLikeByCommentIdAndUserId(commentResponse.getId(), userId)){
                commentResponse.setLikedByCurrentUser(true);
            }
            else commentResponse.setLikedByCurrentUser(false);
        }

        log.info("✅ Trả về {} bình luận cho postId={}", commentResponses.size(), postId);
        return ResponseHandler.generateResponse(Messages.COMMENT_REPLIES_SUCCESS, HttpStatus.OK, commentResponses);
    }

    public ResponseEntity<ApiResponse<List<CommentResponse>>> getRepliesByComment(Long commentId,Long userId,int page, int size) {
        log.info("📥 Lấy danh sách trả lời cho commentId={} | trang={} | kích thước={}", commentId, page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<Comment> replies = commentRepository.findByParentIdAndIsDeletedFalse(commentId, pageable);

        if (replies.isEmpty()) {
            log.warn("⚠️ Không tìm thấy trả lời nào cho commentId={}", commentId);
        } else {
            log.debug("🔍 Tìm thấy {} trả lời cho commentId={}", replies.getTotalElements(), commentId);
        }

        List<CommentResponse> commentResponses = commentMapper.toCommentResponseList(replies.getContent());

        for (CommentResponse commentResponse: commentResponses){
            commentResponse.setCountComment(commentRepository.countByParentId(commentResponse.getId()));

            if (likeService.getLikeByCommentIdAndUserId(commentResponse.getId(), userId)){
                commentResponse.setLikedByCurrentUser(true);
            }
            else commentResponse.setLikedByCurrentUser(false);

        }

        log.info("✅ Trả về {} trả lời cho commentId={}", commentResponses.size(), commentId);
        return ResponseHandler.generateResponse(Messages.COMMENT_REPLIES_SUCCESS, HttpStatus.OK, commentResponses);
    }


    public ResponseEntity<ApiResponse<String>> createComment(CommentRequest commentRequest) {
        Long parent_id = commentRequest.getParentId();
        Long userId = commentRequest.getUserId();
        Long postId = commentRequest.getPostId();
        String content = commentRequest.getContent();

        log.info("[createComment] Request nhận được: userId={}, postId={}, parentId={}, content={}",
                userId, postId, parent_id, content);

        if (userId == null || postId == null || content == null) {
            log.warn("[createComment] Thiếu dữ liệu bắt buộc (userId={}, postId={}, content={})", userId, postId, content);
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO, HttpStatus.BAD_REQUEST, null);
        }

        UserEntity userEntity = userService.findUserByUserId(userId);
        if (userEntity == null) {
            log.error("[createComment] Không tìm thấy user với userId={}", userId);
            return ResponseHandler.generateResponse(Messages.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);
        }

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUserComment(userEntity);
        comment.setCreatedAt(LocalDateTime.now());

        Post post = postService.getPostById(postId);

        if (parent_id != null) {
            Comment parent = commentRepository.findById(parent_id).orElse(null);
            if (parent == null) {
                log.error("[createComment] Không tìm thấy comment cha với parentId={}", parent_id);
                return ResponseHandler.generateResponse(Messages.COMMENT_NOT_FOUND, HttpStatus.NOT_FOUND, null);
            }
            comment.setLevel(parent.getLevel()+1);
            log.info("[createComment] Đây là reply cho commentId={}", parent_id);
            comment.setParent(parent);
            comment.setPost(post);
        } else {

            if (post == null) {
                log.error("[createComment] Không tìm thấy post với postId={}", postId);
                return ResponseHandler.generateResponse(Messages.POST_NOT_FOUND, HttpStatus.NOT_FOUND, null);
            }
            log.info("[createComment] Đây là comment mới cho postId={}", postId);
            comment.setPost(post);
        }

        commentRepository.save(comment);
        log.info("[createComment] Lưu comment thành công với id={}", comment.getId());

        post.setComments(post.getComments() + 1);
        postService.updatePost(post);
        log.info("[createComment] Đã tăng số lượng comment cho postId={} lên {}", postId, post.getComments());

        return ResponseHandler.generateResponse(Messages.COMMENT_CREATED, HttpStatus.CREATED, null);
    }

    public ResponseEntity<ApiResponse<String>> updateComment(CommentUpdateRequest commentUpdateRequest) {
        Long commentId = commentUpdateRequest.getCommentId();
        String content = commentUpdateRequest.getContent();

        log.info("[updateComment] Request nhận được: commentId={}, content={}", commentId, content);

        if (commentId == null || content == null) {
            log.warn("[updateComment] Thiếu dữ liệu bắt buộc (commentId={}, content={})", commentId, content);
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO, HttpStatus.BAD_REQUEST, null);
        }

        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) {
            log.error("[updateComment] Không tìm thấy comment với id={}", commentId);
            return ResponseHandler.generateResponse(Messages.COMMENT_NOT_FOUND, HttpStatus.NOT_FOUND, null);
        }

        comment.setContent(content);
        commentRepository.save(comment);
        log.info("[updateComment] Cập nhật comment thành công với id={}", commentId);

        return ResponseHandler.generateResponse(Messages.UPDATE_COMMENT_SUCCESS, HttpStatus.OK, null);
    }

    public ResponseEntity<ApiResponse<String>> deleteComment(Long commentId) {
        log.info("[deleteComment] Request nhận được: commentId={}", commentId);

        if (commentId == null) {
            log.warn("[deleteComment] Thiếu dữ liệu bắt buộc (commentId=null)");
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO, HttpStatus.BAD_REQUEST, null);
        }

        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) {
            log.error("[deleteComment] Không tìm thấy comment với id={}", commentId);
            return ResponseHandler.generateResponse(Messages.COMMENT_NOT_FOUND, HttpStatus.NOT_FOUND, null);
        }

        comment.setDeleted(true);
        commentRepository.save(comment);
        log.info("[deleteComment] Xóa comment (đánh dấu deleted=true) thành công với id={}", commentId);

        return ResponseHandler.generateResponse(Messages.DELETE_COMMENT, HttpStatus.OK, null);
    }

}
