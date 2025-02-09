package com.example.demo.content.published.reposted;

import com.example.demo.content.published.reposted.dto.CreateOrUpdateRepostDto;
import com.example.demo.content.published.reposted.dto.RepostedDto;
import com.example.demo.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class RepostedArticleController {
    private final RepostedArticleService repostedArticleService;

    @PostMapping("/{articleId}/repost")
    public ResponseEntity<RepostedDto> createRepost(@AuthenticationPrincipal User user,
                                                    @RequestBody CreateOrUpdateRepostDto createRepostedDto,
                                                    @PathVariable Long articleId) {
        return ResponseEntity.ok(repostedArticleService.createRepost(createRepostedDto, articleId, user));
    }

    @GetMapping("/{articleId}/repost")
    public ResponseEntity<Page<RepostedDto>> getAllRepostByOriginal(
            @PathVariable Long articleId,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(repostedArticleService.getAllRepostsByOriginal(articleId, pageable));
    }


    @GetMapping("/repost/{repostId}")
    public ResponseEntity<RepostedDto> getRepostById(
            @PathVariable Long repostId) {
        return ResponseEntity.ok(repostedArticleService.getById(repostId));
    }

    @DeleteMapping("/repost/{repostId}")
    public ResponseEntity<Void> deleteRepost(
            @AuthenticationPrincipal User user,
            @PathVariable Long repostId) {
        repostedArticleService.deleteRepost(user, repostId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/repost/{repostId}")
    public ResponseEntity<RepostedDto> updateRepost(
            @AuthenticationPrincipal User user,
            @PathVariable Long repostId,
            @RequestBody CreateOrUpdateRepostDto updateRepostDto) {
        return ResponseEntity.ok(repostedArticleService.updateRepost(updateRepostDto, user, repostId));
    }
}
