package com.example.demo.content.published;

import com.example.demo.content.published.article.PublishedArticleService;
import com.example.demo.content.published.article.dto.PublishedDto;
import com.example.demo.content.published.reposted.RepostedArticleService;
import com.example.demo.content.published.reposted.dto.RepostedDto;
import com.example.demo.content.shared.ArticleService;
import com.example.demo.content.shared.dto.ArticleDto;
import com.example.demo.user.User;
import com.example.demo.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/{userId}/articles")
@RequiredArgsConstructor
public class VisibleArticleController {
    private final PublishedArticleService publishedArticleService;
    private final ArticleService articleService;
    private final UserService userService;
    private final RepostedArticleService repostedArticleService;


    @GetMapping
    public ResponseEntity<Page<ArticleDto>> getAllArticlesByUser(
            @PathVariable Long userId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){

        User user = userService.findById(userId);
        return ResponseEntity.ok(articleService.getVisibleArticlesByUser(user, pageable));

    }


    @GetMapping("/publish")
    public ResponseEntity<Page<PublishedDto>> getAllPublishedArticleByUser(
            @PathVariable Long userId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(publishedArticleService.getAllArticlesByUser(userId, pageable));

    }

    @GetMapping("/reposts")
    public ResponseEntity<Page<RepostedDto>> getAllRepostsByUser(
            @PathVariable Long userId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.ok(repostedArticleService.getAllRepostsByUser(userId, pageable));
    }

}
