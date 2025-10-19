package com.example.Qolzy.repository;

import com.example.Qolzy.model.contact.Contact;
import com.example.Qolzy.model.message.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {
    Page<Message> findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByCreatedAtAsc(
            Long sender1, Long receiver1, Long sender2, Long receiver2, Pageable pageable);
    Message findTopBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByCreatedAtDesc(Long senderId1, Long receiverId1,
                                                                                  Long senderId2, Long receiverId2);
}
