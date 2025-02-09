package com.example.demo.content.published.article;

import com.example.demo.content.published.article.dto.CreatePublishedDto;
import com.example.demo.content.published.article.dto.PublishedDto;
import com.example.demo.content.published.article.dto.UpdatePublishedDto;
import com.example.demo.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles/published")
@RequiredArgsConstructor
public class PublishedArticleController {
    private final PublishedArticleService publishedArticleService;

    @PostMapping
    public ResponseEntity<PublishedDto> createPublishedArticle(
            @AuthenticationPrincipal User user,
            @RequestBody CreatePublishedDto createPublishedDto) {
        return ResponseEntity.ok(publishedArticleService.createArticle(createPublishedDto, user));
    }

    @GetMapping("/{publishedId}")
    public ResponseEntity<PublishedDto> getPublishedArticlesById(
            @PathVariable Long publishedId){
        return ResponseEntity.ok(publishedArticleService.getArticleById(publishedId));
    }

    @PutMapping("/{publishedId}")
    public ResponseEntity<PublishedDto> updatePublishedArticle(
            @AuthenticationPrincipal User user,
            @PathVariable Long publishedId,
            @RequestBody UpdatePublishedDto updatePublishedDto){
        return ResponseEntity.ok(
                publishedArticleService.updateArticle(updatePublishedDto, publishedId, user)
        );
    }

    @DeleteMapping("/{publishedId}")
    public ResponseEntity<Void> deletePublishedArticle(
            @AuthenticationPrincipal User user,
            @PathVariable Long publishedId){
        publishedArticleService.deleteArticleById(publishedId, user);
        return ResponseEntity.noContent().build();
    }
}
