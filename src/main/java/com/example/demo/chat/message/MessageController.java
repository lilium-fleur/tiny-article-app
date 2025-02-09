package com.example.demo.chat.message;

import com.example.demo.chat.message.dto.CreateMessageDto;
import com.example.demo.chat.message.dto.MessageDto;
import com.example.demo.chat.message.dto.MessageResponse;
import com.example.demo.chat.message.dto.UpdateMessageDto;
import com.example.demo.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats/{chatId}")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageDto> sendMessage(
            @AuthenticationPrincipal User user,
            @PathVariable Long chatId,
            @RequestBody CreateMessageDto createMessageDto) {
        return ResponseEntity.ok(messageService.sendMessage(createMessageDto, chatId, user));
    }

    @PutMapping("/messages/{messageId}")
    public ResponseEntity<MessageDto> updateMessage(
            @AuthenticationPrincipal User user,
            @PathVariable Long messageId,
            @RequestBody UpdateMessageDto updateMessageDto) {
        return ResponseEntity.ok(messageService.updateMessage(updateMessageDto, messageId, user));
    }


    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Void> deleteMessage(
            @AuthenticationPrincipal User user,
            @PathVariable Long messageId){
        messageService.deleteMessage(messageId, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/messages/{strategy}/{cursor}/limit/{limit}")
    public ResponseEntity<MessageResponse> getMessages(
            @AuthenticationPrincipal User user,
            @PathVariable Long chatId,
            @PathVariable LoadStrategy strategy,
            @PathVariable(required = false) Long cursor,
            @PathVariable Integer limit) {
        return ResponseEntity.ok(messageService.getMessages(user, chatId, cursor, limit, strategy));
    }



}
