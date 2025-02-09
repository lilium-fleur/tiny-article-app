package com.example.demo.reaction.shared;

import com.example.demo.reaction.dislike.DislikeService;
import com.example.demo.reaction.dislike.dto.DislikeDto;
import com.example.demo.reaction.like.LikeService;
import com.example.demo.reaction.like.dto.LikeDto;
import com.example.demo.reaction.shared.dto.ReactionDto;
import com.example.demo.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles/{articleId}/reactions")
@RequiredArgsConstructor
public class ReactionController {

    private final LikeService likeService;
    private final DislikeService dislikeService;

    @GetMapping
    public ResponseEntity<ReactionDto> getReactions(@PathVariable("articleId") Long articleId) {
        ReactionDto reactionDto = ReactionDto.builder()
                .articleId(articleId)
                .Likes(likeService.getAllLikes(articleId))
                .Dislikes(dislikeService.getAllDislikes(articleId))
                .build();

        return ResponseEntity.ok(reactionDto);
    }

    @PostMapping("/likes")
    public ResponseEntity<LikeDto> addLike(
            @AuthenticationPrincipal User user,
            @PathVariable("articleId") Long articleId) {

        return ResponseEntity.ok(likeService.addLike(articleId, user));
    }

    @PostMapping("/dislikes")
    public ResponseEntity<DislikeDto> addDislike(
            @AuthenticationPrincipal User user,
            @PathVariable("articleId") Long articleId){

        return ResponseEntity.ok(dislikeService.addDislike(articleId, user));
    }


    @DeleteMapping("/likes")
    public ResponseEntity<Void> removeLike(
            @AuthenticationPrincipal User user,
            @PathVariable Long articleId) {
        likeService.removeLike(articleId, user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/dislikes")
    public ResponseEntity<Void> removeDislike(
            @AuthenticationPrincipal User user,
            @PathVariable Long articleId){
        dislikeService.removeDislike(articleId, user);
        return ResponseEntity.noContent().build();
    }


}
