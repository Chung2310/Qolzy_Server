package com.example.Qolzy.controller;

import com.example.Qolzy.model.ApiResponse;
import com.example.Qolzy.model.ResponseHandler;
import com.example.Qolzy.model.message.MessageRequest;
import com.example.Qolzy.model.message.MessageResponse;
import com.example.Qolzy.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/message")
@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping()
    @MessageMapping("/send")
    @SendToUser("/topic/messages")
    public ResponseEntity<ApiResponse<String>> sendMessage(@RequestBody MessageRequest messageRequest) {
        return messageService.sendMessage(messageRequest);
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getMessages(@RequestParam Long senderId,
                                                                          @RequestParam Long receiverId,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "20") int size){
        return messageService.getHistoryMessages(senderId, receiverId, page, size);
    }
}
