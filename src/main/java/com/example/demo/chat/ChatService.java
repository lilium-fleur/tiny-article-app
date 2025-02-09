package com.example.demo.chat;

import com.example.demo.chat.dto.ChatDto;
import com.example.demo.user.User;
import com.example.demo.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;
    private final ChatMapper chatMapper;

    @Transactional
    public ChatDto createChat(User sender, Long recipientId) {
        User recipient = userService.findById(recipientId);

        return chatRepository.findByUser1AndUser2(sender, recipient)
                .map(chatMapper::toDto)
                .orElseGet(() -> {
                    Chat chat = Chat.builder()
                            .user1(sender)
                            .user2(recipient)
                            .build();
                    return chatMapper.toDto(chatRepository.save(chat));
                });
    }

    @Transactional(readOnly = true)
    public Chat findChatByIdAndUser(Long chatId, User user){
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat not found"));

        if(!chat.getUser1().getId().equals(user.getId()) && !chat.getUser2().getId().equals(user.getId())){
            throw new AccessDeniedException("Permission denied");
        }

        return chat;
    }

    @Transactional(readOnly = true)
    public List<ChatDto> getAllChats(User user){
        return chatRepository.findAllByUser(user).stream()
                .map(chatMapper::toDto)
                .toList();
    }

    @Transactional
    public void deleteChat(Long chatId, User user){
        Chat chat = findChatByIdAndUser(chatId, user);
        chatRepository.delete(chat);
    }


}

