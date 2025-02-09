package com.example.demo.comment;

import com.example.demo.comment.dto.CommentDto;
import com.example.demo.comment.dto.CreateOrUpdateCommentDto;
import com.example.demo.content.shared.ArticleService;
import com.example.demo.content.shared.BaseArticle;
import com.example.demo.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ArticleService articleService;


    @Transactional(readOnly = true)
    public Page<CommentDto> getAllCommentsByArticle(Long articleId,
                                                    Pageable pageable){

        return commentRepository.findAllByArticleId(articleId, pageable)
                .map(commentMapper::toDto);
    }


    @Transactional
    public CommentDto createComment(User user, Long articleId, CreateOrUpdateCommentDto createCommentDto) {
        BaseArticle article = articleService.getVisibleArticleEntityById(articleId);
        Comment comment = Comment.builder()
                .comment(createCommentDto.comment())
                .article(article)
                .author(user)
                .build();

        Comment createdComment = commentRepository.save(comment);

        return commentMapper.toDto(createdComment);
    }

    @Transactional
    public CommentDto createReplyToComment(User user, Long commentId, CreateOrUpdateCommentDto createCommentDto) {
        Comment commentForReply = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        Comment reply = Comment.builder()
                .comment(createCommentDto.comment())
                .author(user)
                .article(commentForReply.getArticle())
                .reply(commentForReply)
                .build();

        Comment savedReply = commentRepository.save(reply);

        return commentMapper.toDto(savedReply);

    }

    @Transactional
    public CommentDto updateComment(User user, Long commentId, CreateOrUpdateCommentDto updateCommentDto) {
        Comment comment = getCommentByUserOrElseThrowException(user, commentId);

        comment.setComment(updateCommentDto.comment());

        Comment updatedComment = commentRepository.save(comment);
        return commentMapper.toDto(updatedComment);
    }

    @Transactional
    public void deleteComment(User user, Long commentId) {
        Comment comment = getCommentByUserOrElseThrowException(user, commentId);
        commentRepository.delete(comment);
    }

    private Comment getCommentByUserOrElseThrowException(User user, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        if(!Objects.equals(comment.getAuthor().getId(), user.getId())){
            throw new AccessDeniedException("You do not have permission to edit this comment");
        }
        return comment;
    }

}
