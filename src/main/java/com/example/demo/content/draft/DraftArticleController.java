package com.example.demo.content.draft;

import com.example.demo.content.draft.dto.CreateDraftDto;
import com.example.demo.content.draft.dto.DraftDto;
import com.example.demo.content.draft.dto.UpdateDraftDto;
import com.example.demo.content.published.article.dto.PublishedDto;
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

@RestController
@RequestMapping("/api/articles/drafts")
@RequiredArgsConstructor
public class DraftArticleController {

    private final DraftArticleService draftArticleService;

    @PostMapping
    public ResponseEntity<DraftDto> crateDraft(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateDraftDto createDraftDto){

        return ResponseEntity.ok(draftArticleService.createDraft(createDraftDto, user));
    }

    @GetMapping
    public ResponseEntity<Page<DraftDto>> getAllDrafts(
            @AuthenticationPrincipal User user,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){

        return ResponseEntity.ok(draftArticleService.getAllDraftByUser(user, pageable));

    }

    @GetMapping("/{draftId}")
    public ResponseEntity<DraftDto> getDraftById(
            @AuthenticationPrincipal User user,
            @PathVariable Long draftId){

        return ResponseEntity.ok(draftArticleService.getDraftById(draftId, user));
    }

    @DeleteMapping("/{draftId}")
    public ResponseEntity<Void> deleteDraft(
            @AuthenticationPrincipal User user,
            @PathVariable Long draftId){

        draftArticleService.deleteDraftById(draftId, user);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{draftId}")
    public ResponseEntity<DraftDto> updateDraft(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdateDraftDto updateDto,
            @PathVariable Long draftId){

        return ResponseEntity.ok(draftArticleService.updateDraft(updateDto, draftId, user));

    }

    @PostMapping("/{draftId}/publish")
    public ResponseEntity<PublishedDto> publishDraft(
            @AuthenticationPrincipal User user,
            @PathVariable Long draftId){
        return ResponseEntity.ok(draftArticleService.publishDraft(draftId, user));
    }


}
