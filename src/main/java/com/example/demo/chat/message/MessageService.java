package com.example.demo.chat.message;

import com.example.demo.chat.Chat;
import com.example.demo.chat.ChatService;
import com.example.demo.chat.message.dto.*;
import com.example.demo.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatService chatService;
    private final MessageMapper messageMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private static final int MAX_LIMIT = 50;


    @Transactional
    public MessageDto sendMessage(CreateMessageDto createMessageDto, Long chatId, User sender) {
        Chat chat = chatService.findChatByIdAndUser(chatId, sender);
        User recipient = getRecipient(chat, sender);

        Message message = Message.builder()
                .content(createMessageDto.content())
                .sender(sender)
                .recipient(recipient)
                .chat(chat)
                .createdAt(LocalDateTime.now())
                .build();

        Message saved = messageRepository.save(message);

        sendToUser(saved);

        return messageMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public MessageResponse getMessages(User user, Long chatId, GetMessagesFilter filter) {
        Chat chat = chatService.findChatByIdAndUser(chatId, user);
        int normalizedLimit = Math.min(filter.getLimit(), MAX_LIMIT);

        return switch (filter.getStrategy()) {
            case LATEST -> getLatestMessages(chat, normalizedLimit);
            case BEFORE_CURSOR -> getMessagesBefore(chat, filter.getCursor(), normalizedLimit);
            case AFTER_CURSOR -> getMessagesAfter(chat, filter.getCursor(), normalizedLimit);
        };
    }

    @Transactional
    public void deleteMessage(Long messageId, User user) {

        Message message = getMessageOrThrowException(messageId, user);

        messageRepository.deleteById(messageId);

        messagingTemplate.convertAndSendToUser(message.getRecipient().getId().toString(), "/queue/messages",
                DeletedMessageDto.builder()
                        .id(message.getId())
                        .chat(message.getChat().getId())
                        .deleted(true)
                        .build());
    }

    @Transactional
    public MessageDto updateMessage(UpdateMessageDto updateMessageDto, Long messageId, User user) {
        Message message = getMessageOrThrowException(messageId, user);
        message.setContent(updateMessageDto.content());
        Message saved = messageRepository.save(message);

        sendToUser(saved);

        return messageMapper.toDto(saved);
    }


    private MessageResponse getLatestMessages(Chat chat, int limit) {
        List<Message> messages = messageRepository.findLatestByChat(
                chat,
                PageRequest.of(0, limit));

        return processMessages(messages, limit);
    }

    private MessageResponse getMessagesAfter(Chat chat, Long cursor, int limit) {
        List<Message> messages = messageRepository.findByChatAndIdGreaterThan(
                chat,
                cursor,
                PageRequest.of(0, limit + 1));
        System.out.println(messages);


        return processMessages(messages, limit);
    }

    private MessageResponse getMessagesBefore(Chat chat, Long cursor, int limit) {
        List<Message> messages = messageRepository.findByChatAndIdLessThan(
                chat,
                cursor,
                PageRequest.of(0, limit + 1));

        return processMessages(messages, limit);
    }

    private MessageResponse processMessages(List<Message> messages, int limit) {
        boolean hasMore = messages.size() > limit;
        List<Message> resultMessages = hasMore ? messages.subList(0, limit) : messages;

        Long nextCursor = !resultMessages.isEmpty() ? resultMessages.getLast().getId() : null;

        return MessageResponse.builder()
                .messages(resultMessages.stream()
                        .map(messageMapper::toDto)
                        .toList())
                .nextCursor(nextCursor)
                .hasMore(hasMore)
                .build();
    }

    private User getRecipient(Chat chat, User sender) {
        User recipient;

        if (chat.getUser1().getId().equals(sender.getId())) {
            recipient = chat.getUser2();
        } else {
            recipient = chat.getUser1();
        }

        return recipient;
    }

    private Message getMessageOrThrowException(Long messageId, User user) {

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new EntityNotFoundException("Message not found"));

        if (!message.getSender().getId().equals(user.getId())) {
            throw new AccessDeniedException("Permission denied");
        }
        return message;
    }

    private void sendToUser(Message message) {
        messagingTemplate.convertAndSendToUser(message.getRecipient().getId().toString(), "/queue/messages",
                MessageDto.builder()
                        .id(message.getId())
                        .content(message.getContent())
                        .sender(message.getSender().getId())
                        .recipient(message.getRecipient().getId())
                        .chat(message.getChat().getId())
                        .createdAt(message.getCreatedAt())
                        .build());
    }
}
