package com.example.Qolzy.service;

import com.example.Qolzy.mapper.MessageMapper;
import com.example.Qolzy.mapper.UserMapper;
import com.example.Qolzy.model.ApiResponse;
import com.example.Qolzy.model.Messages;
import com.example.Qolzy.model.ResponseHandler;
import com.example.Qolzy.model.auth.UserEntity;
import com.example.Qolzy.model.contact.Contact;
import com.example.Qolzy.model.contact.ContactRequest;
import com.example.Qolzy.model.message.Message;
import com.example.Qolzy.model.message.MessageRequest;
import com.example.Qolzy.model.message.MessageResponse;
import com.example.Qolzy.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private ContactService contactService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public ResponseEntity<ApiResponse<String>> sendMessage(MessageRequest messageRequest) {
        log.info(" Gửi tin nhắn từ id: {} đến id: {} với nôi dung: {}", messageRequest.getSenderId(), messageRequest.getReceiverId(), messageRequest.getContent());

        Long senderId = messageRequest.getSenderId();
        Long receiverId = messageRequest.getReceiverId();
        String content = messageRequest.getContent();

        if (senderId == 0 || receiverId == 0 || content == null || content.trim().isEmpty()) {
            log.warn(" Thiếu thông tin cần thiết khi gửi tin nhắn. senderId={}, receiverId={}, content={}", senderId, receiverId, content);
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO, HttpStatus.BAD_REQUEST, null);
        }

        UserEntity sender = userService.findUserByUserId(senderId);
        UserEntity receiver = userService.findUserByUserId(receiverId);

        if (sender == null || receiver == null) {
            log.error(" Không tìm thấy người dùng. senderId={}, receiverId={}", senderId, receiverId);
            return ResponseHandler.generateResponse(Messages.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);
        }

        Message message = new Message();
        message.setCreatedAt(LocalDateTime.now());
        message.setContent(content);
        message.setSender(sender);
        message.setReceiver(receiver);

        if (contactService.checkContact(senderId, receiverId)) {
            Contact contact = contactService.findContact(senderId,receiverId);
            contact.setLastMessage(content);
            contact.setLastTime(LocalDateTime.now());
            contactService.saveContact(contact);
        }
        else {
            ContactRequest contactRequest = new ContactRequest();
            contactRequest.setUserId(senderId);
            contactRequest.setContactId(receiverId);
            contactRequest.setContent(content);
            contactService.createContact(contactRequest);

            contactRequest.setUserId(receiverId);
            contactRequest.setContactId(senderId);
            contactService.createContact(contactRequest);
        }

        try {
            messageRepository.save(message);
            log.info(" Đã lưu tin nhắn ID={} từ {} -> {}", message.getId(), sender.getLastName(), receiver.getLastName());
        } catch (Exception e) {
            log.error(" Lỗi khi lưu tin nhắn: {}", e.getMessage(), e);
            return ResponseHandler.generateResponse(Messages.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, null);
        }

        try {
            template.convertAndSendToUser(
                    message.getReceiver().getId().toString(),
                    "/topic/messages",
                    message
            );
            log.info(" Đã gửi tin nhắn qua WebSocket tới userId={}", message.getReceiver().getId());
        } catch (Exception e) {
            log.error(" Lỗi khi gửi tin qua WebSocket: {}", e.getMessage(), e);
        }

        return ResponseHandler.generateResponse(Messages.CHAT_MESSAGE_SENT_SUCCESS, HttpStatus.OK, null);
    }

    public ResponseEntity<ApiResponse<List<MessageResponse>>> getHistoryMessages(Long senderId, Long receiverId, int page, int size) {
        log.info(" Yêu cầu lấy lịch sử chat giữa senderId={} và receiverId={} (page={}, size={})",
                senderId, receiverId, page, size);

        if (senderId == 0 || receiverId == 0) {
            log.warn("️ Thiếu thông tin khi lấy lịch sử tin nhắn.");
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO, HttpStatus.BAD_REQUEST, null);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Message> messages;

        try {
            messages = messageRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByCreatedAtAsc(
                    senderId, receiverId,
                    receiverId, senderId,
                    pageable
            );
            log.info(" Tìm thấy {} tin nhắn trong lịch sử.", messages.getTotalElements());
        } catch (Exception e) {
            log.error(" Lỗi khi truy vấn lịch sử tin nhắn: {}", e.getMessage(), e);
            return ResponseHandler.generateResponse(Messages.SYSTEM_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, null);
        }

        List<Message> messageList = messages.getContent();
        List<MessageResponse> responseList = messageMapper.toMessageResponseList(messageList);

        return ResponseHandler.generateResponse(Messages.CHAT_HISTORY_FETCH_SUCCESS, HttpStatus.OK, responseList);
    }


}

