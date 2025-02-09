package com.example.demo.content.draft;

import com.example.demo.content.draft.dto.CreateDraftDto;
import com.example.demo.content.draft.dto.DraftDto;
import com.example.demo.content.draft.dto.UpdateDraftDto;
import com.example.demo.content.published.article.PublishedArticleService;
import com.example.demo.content.published.article.dto.CreatePublishedDto;
import com.example.demo.content.published.article.dto.PublishedDto;
import com.example.demo.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DraftArticleService {
    private final DraftArticleRepository draftArticleRepository;
    private final DraftArticleMapper draftArticleMapper;
    private final PublishedArticleService publishedArticleService;


    @Transactional(readOnly = true)
    public DraftDto getDraftById(Long draftId, User user) {
        DraftArticle article = getDraftForEditOrThrowException(draftId, user);
        return draftArticleMapper.toDto(article);
    }

    @Transactional(readOnly = true)
    public Page<DraftDto> getAllDraftByUser(User user, Pageable pageable) {
        return draftArticleRepository.findAllByAuthorId(user.getId(), pageable)
                .map(draftArticleMapper::toDto);
    }

    @Transactional
    public DraftDto createDraft(CreateDraftDto createDraftDto, User user) {
        DraftArticle article = DraftArticle.builder()
                .title(createDraftDto.title())
                .content(createDraftDto.content())
                .author(user)
                .build();
        return draftArticleMapper.toDto(draftArticleRepository.save(article));
    }


    @Transactional
    public void deleteDraftById(Long draftId, User user) {
        DraftArticle article = getDraftForEditOrThrowException(draftId, user);
        draftArticleRepository.delete(article);

    }

    @Transactional
    public DraftDto updateDraft(UpdateDraftDto updateArticleDto, Long draftId, User user) {
        DraftArticle draft = getDraftForEditOrThrowException(draftId, user);

        Optional.ofNullable(updateArticleDto.title())
                .ifPresent(draft::setTitle);
        Optional.ofNullable(updateArticleDto.content())
                .ifPresent(draft::setContent);

        return draftArticleMapper.toDto(draftArticleRepository.save(draft));
    }

    public PublishedDto publishDraft(Long draftId, User user) {
        DraftArticle draft = getDraftForEditOrThrowException(draftId, user);

        CreatePublishedDto createPublishedDto = CreatePublishedDto.builder()
                .title(draft.getTitle())
                .content(draft.getContent())
                .build();

        draftArticleRepository.delete(draft);
        return publishedArticleService.createArticle(createPublishedDto, user);
    }


    private DraftArticle getDraftForEditOrThrowException(Long articleId, User user) {
        DraftArticle draft = draftArticleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("Article not found"));

        if(!draft.getAuthor().getId().equals(user.getId())) {
            throw new AccessDeniedException("Permission denied");
        }

        return draft;
    }

}
