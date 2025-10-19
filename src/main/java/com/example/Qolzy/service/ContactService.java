package com.example.Qolzy.service;

import com.example.Qolzy.mapper.ContactMapper;
import com.example.Qolzy.model.ApiResponse;
import com.example.Qolzy.model.Messages;
import com.example.Qolzy.model.ResponseHandler;
import com.example.Qolzy.model.auth.UserEntity;
import com.example.Qolzy.model.contact.Contact;
import com.example.Qolzy.model.contact.ContactRequest;
import com.example.Qolzy.model.contact.ContactResponse;
import com.example.Qolzy.model.message.Message;
import com.example.Qolzy.repository.ContactRepository;
import com.example.Qolzy.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContactService {
    private static final Logger log = LoggerFactory.getLogger(ContactService.class);

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ContactMapper contactMapper;

    public boolean checkContact(Long userId, Long contactId) {
        log.info("Kiểm tra liên hệ giữa userId={} và contactId={}", userId, contactId);
        boolean exists = contactRepository.existsByUserIdAndUserContactId(userId, contactId);
        log.debug("Kết quả kiểm tra: {}", exists);
        return exists;
    }

    public Contact findContact(Long userId, Long contactId) {
        log.info("Tìm liên hệ giữa userId={} và contactId={}", userId, contactId);
        Contact contact = contactRepository.findContactsByUserIdAndUserContactId(userId, contactId);
        if (contact == null) {
            log.warn("Không tìm thấy liên hệ giữa {} và {}", userId, contactId);
        } else {
            log.debug("Đã tìm thấy liên hệ ID={}", contact.getId());
        }
        return contact;
    }

    public void saveContact(Contact contact) {
        log.info("Lưu liên hệ: userId={} <-> contactId={}",
                contact.getUser().getId(), contact.getUserContact().getId());
        contactRepository.save(contact);
        log.debug("Đã lưu liên hệ thành công vào database.");
    }

    public ResponseEntity<ApiResponse<String>> createContact(ContactRequest contactRequest) {
        Long userId = contactRequest.getUserId();
        Long contactId = contactRequest.getContactId();
        String content = contactRequest.getContent();

        log.info("Yêu cầu tạo liên hệ: userId={}, contactId={}, content={}", userId, contactId, content);

        // Kiểm tra thông tin bắt buộc
        if (userId == null || contactId == null || content == null) {
            log.warn("Thiếu thông tin bắt buộc khi tạo liên hệ.");
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO, HttpStatus.BAD_REQUEST, null);
        }

        UserEntity user = userService.findUserByUserId(userId);
        UserEntity userContact = userService.findUserByUserId(contactId);

        if (user == null || userContact == null) {
            log.warn("Không tìm thấy user hoặc contact trong hệ thống: userId={}, contactId={}", userId, contactId);
            return ResponseHandler.generateResponse(Messages.USER_NOT_FOUND, HttpStatus.NOT_FOUND, null);
        }

        // Nếu liên hệ đã tồn tại thì không tạo lại
        if (checkContact(userId, contactId)) {
            log.info("Liên hệ giữa userId={} và contactId={} đã tồn tại.", userId, contactId);
            return ResponseHandler.generateResponse(Messages.CONTACT_ALREADY_EXISTS, HttpStatus.CONFLICT, null);
        }

        Contact contact = new Contact();
        contact.setUser(user);
        contact.setUserContact(userContact);
        contact.setLastMessage(content);
        contact.setLastTime(LocalDateTime.now());

        contactRepository.save(contact);

        log.info("Đã tạo liên hệ mới giữa userId={} và contactId={}", userId, contactId);

        return ResponseHandler.generateResponse(Messages.CONTACT_CREATED_SUCCESS, HttpStatus.CREATED, null);
    }

    public ResponseEntity<ApiResponse<List<ContactResponse>>> getContacts(Long userId, int page, int size) {
        log.info(" Lấy danh sách liên hệ của userId={}, page={}, size={}", userId, page, size);

        // Kiểm tra tham số đầu vào
        if (userId == null) {
            log.warn("️ Thiếu userId khi lấy danh sách liên hệ.");
            return ResponseHandler.generateResponse(Messages.MISSING_REQUIRED_INFO, HttpStatus.BAD_REQUEST, null);
        }

        // Tạo PageRequest
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Contact> contactPage = contactRepository.findByUserId(userId, pageRequest);
        List<Contact> contacts = contactPage.getContent();

        log.info(" Đã lấy {} liên hệ từ DB cho userId={}", contacts.size(), userId);

        // Map sang DTO phản hồi
        List<ContactResponse> contactResponses = contactMapper.toResponseList(contacts);

        // Lấy tin nhắn cuối cùng cho từng liên hệ
        for (ContactResponse contactResponse : contactResponses) {
            Long contactId = contactResponse.getUserContact().getId();
            log.debug("️ Đang lấy tin nhắn gần nhất giữa userId={} và contactId={}", userId, contactId);

            // Lấy message mới nhất giữa 2 người (dù ai gửi)
            Message message = messageRepository
                    .findTopBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByCreatedAtDesc(
                            userId, contactId, contactId, userId);

            if (message != null) {
                boolean isSentByCurrentUser = userId.equals(message.getSender().getId());
                contactResponse.setCurrentUserLastMessage(isSentByCurrentUser);
                contactResponse.setLastMessage(message.getContent());
                contactResponse.setLastTime(message.getCreatedAt());

                log.debug(" Tin nhắn cuối: '{}' (người gửi: {}, thời gian: {})",
                        message.getContent(),
                        message.getSender().getId(),
                        message.getCreatedAt());
            } else {
                log.debug(" Không tìm thấy tin nhắn nào giữa userId={} và contactId={}", userId, contactId);
            }
        }

        log.info(" Hoàn tất lấy danh sách liên hệ cho userId={}, tổng số: {}", userId, contactResponses.size());
        return ResponseHandler.generateResponse(Messages.CHAT_HISTORY_FETCH_SUCCESS, HttpStatus.OK, contactResponses);
    }

}
