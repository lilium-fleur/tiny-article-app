package com.example.demo.chat;

import com.example.demo.chat.dto.ChatDto;
import com.example.demo.chat.message.MessageService;
import com.example.demo.chat.message.dto.MessageDto;
import com.example.demo.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final MessageService messageService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<ChatDto> createChat(
            @PathVariable Long userId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(chatService.createChat(user, userId));
    }

    @GetMapping
    public ResponseEntity<List<ChatDto>> getChats(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(chatService.getAllChats(user));
    }


}
