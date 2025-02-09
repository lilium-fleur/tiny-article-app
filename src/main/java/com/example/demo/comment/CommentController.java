package com.example.demo.comment;

import com.example.demo.comment.dto.CreateOrUpdateCommentDto;
import com.example.demo.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.demo.comment.dto.CommentDto;


@RestController
@RequestMapping("/api/articles/{articleId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<Page<CommentDto>> getAllCommentsByArticle(
            @PathVariable Long articleId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.ok(commentService.getAllCommentsByArticle(articleId, pageable));
    }

    @PostMapping
    public ResponseEntity<CommentDto> createComment(
            @AuthenticationPrincipal User user,
            @PathVariable Long articleId,
            @Valid @RequestBody CreateOrUpdateCommentDto createCommentDto) {

        return ResponseEntity.ok(commentService.createComment(user, articleId, createCommentDto));
    }

    @PostMapping("/{commentId}")
    public ResponseEntity<CommentDto> replyToComment(
            @AuthenticationPrincipal User user,
            @PathVariable Long commentId,
            @RequestBody CreateOrUpdateCommentDto createCommentDto) {

        return ResponseEntity.ok(commentService.createReplyToComment(user, commentId, createCommentDto));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @AuthenticationPrincipal User user,
            @PathVariable Long commentId,
            @Valid @RequestBody CreateOrUpdateCommentDto updateCommentDto){

        return ResponseEntity.ok(commentService.updateComment(user, commentId, updateCommentDto));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @AuthenticationPrincipal User user,
            @PathVariable Long commentId){
        commentService.deleteComment(user, commentId);
        return ResponseEntity.noContent().build();
    }

}
