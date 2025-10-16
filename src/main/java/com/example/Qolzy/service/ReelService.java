package com.example.Qolzy.service;

import com.example.Qolzy.mapper.ReelMapper;
import com.example.Qolzy.model.ApiResponse;
import com.example.Qolzy.model.Messages;
import com.example.Qolzy.model.ResponseHandler;
import com.example.Qolzy.model.post.Post;
import com.example.Qolzy.model.reel.Reel;
import com.example.Qolzy.model.reel.ReelResponse;
import com.example.Qolzy.repository.PostRepository;
import com.example.Qolzy.repository.ReelRepository;
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
public class ReelService {
    @Autowired
    private ReelRepository reelRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReelMapper reelMapper;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public void createReels(Long postId, String media) {
        log.info("Bắt đầu tạo Reel từ postId={} với media={}", postId, media);

        Post post = postRepository.findPostById(postId);
        if (post == null) {
            log.warn("Không tìm thấy Post với id={}", postId);
            return;
        }

        Reel reel = new Reel();
        reel.setUser(post.getUser());
        reel.setContent(post.getContent());
        reel.setMedia(media);

        reelRepository.save(reel);
        log.info("Tạo Reel thành công với id={} từ postId={}", reel.getId(), postId);
    }

    public ResponseEntity<ApiResponse<List<ReelResponse>>> getReels(Long userId, int page, int size) {
        log.info("Lấy Reels cho userId={} page={} size={}", userId, page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<Reel> reelPage = reelRepository.findByIsDeletedFalse(pageable);

        List<Reel> reelList = reelPage.getContent();
        log.info("Số Reels lấy được: {}", reelList.size());

        List<ReelResponse> reelResponses = reelMapper.toReelResponses(reelList);
        return ResponseHandler.generateResponse(Messages.DATA_FETCH_SUCCESS, HttpStatus.OK, reelResponses);
    }
}
