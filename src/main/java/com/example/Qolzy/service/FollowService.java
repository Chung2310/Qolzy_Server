package com.example.Qolzy.service;

import com.example.Qolzy.mapper.FollowMapper;
import com.example.Qolzy.model.*;
import com.example.Qolzy.model.auth.UserEntity;
import com.example.Qolzy.model.follow.Follow;
import com.example.Qolzy.model.follow.FollowResponse;
import com.example.Qolzy.repository.FollowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowService {
    private static final Logger log = LoggerFactory.getLogger(FollowService.class);

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FollowMapper followMapper;

    public List<Follow> getFollowsByUserId(Long userId) {
        return followRepository.findByFollowerId(userId);
    }

    public ResponseEntity<ApiResponse<String>> createFollow(Long followerId, Long followingId) {
        log.info("[Follow] Yêu cầu follow/unfollow từ user {} -> user {}", followerId, followingId);

        if (followerId == null || followingId == null) {
            log.warn("[Follow] Thiếu followerId hoặc followingId");
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO, HttpStatus.BAD_REQUEST, null);
        }

        // Nếu đã tồn tại follow thì unfollow
        if (followRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
            log.info("[Follow] User {} đã hủy follow user {}", followerId, followingId);
            return ResponseHandler.generateResponse(Messages.FOLLOW_DELETE, HttpStatus.OK, null);
        }

        // Kiểm tra user tồn tại
        UserEntity userFollower = userService.findUserByUserId(followerId);
        UserEntity userFollowing = userService.findUserByUserId(followingId);

        if (userFollower == null) {
            log.error("[Follow] Không tìm thấy user follower với id {}", followerId);
            return ResponseHandler.generateResponse(Messages.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);
        }
        if (userFollowing == null) {
            log.error("[Follow] Không tìm thấy user following với id {}", followingId);
            return ResponseHandler.generateResponse(Messages.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);
        }

        // Tạo follow mới
        Follow follow = new Follow();
        follow.setFollower(userFollower);
        follow.setFollowing(userFollowing);
        followRepository.save(follow);

        log.info("[Follow] User {} đã follow user {}", followerId, followingId);
        return ResponseHandler.generateResponse(Messages.FOLLOW_CREATE, HttpStatus.CREATED, null);
    }

    public ResponseEntity<ApiResponse<List<FollowResponse>>> getFollows(Long userId, int page, int size) {
        log.info(" Bắt đầu lấy danh sách follow cho userId: {}", userId);

        if (userId == null) {
            log.warn(" userId bị null, không thể lấy danh sách follow");
            return ResponseHandler.generateResponse(
                    Messages.MISSING_REQUIRED_INFO,
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Follow> follows = followRepository.findByFollowerId(userId, pageable);

        List<Follow> followList = follows.getContent();
        log.debug("Tìm thấy {} follow từ DB cho userId {}", followList.size(), userId);

        List<FollowResponse> responseList = followMapper.toFollowResponseList(followList);
        log.info(" Trả về {} follow response cho userId {}", responseList.size(), userId);

        return ResponseHandler.generateResponse(
                Messages.FOLLOW_FETCH_SUCCESS,
                HttpStatus.OK,
                responseList
        );
    }
}
