package com.example.demo.content.published.reposted;

import com.example.demo.content.published.article.PublishedArticle;
import com.example.demo.content.published.article.PublishedArticleService;
import com.example.demo.content.published.reposted.dto.CreateOrUpdateRepostDto;
import com.example.demo.content.published.reposted.dto.RepostedDto;
import com.example.demo.user.User;
import io.micrometer.common.util.StringUtils;
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
public class RepostedArticleService {
    private final RepostedArticleRepository repostedArticleRepository;
    private final PublishedArticleService publishedArticleService;
    private final RepostedArticleMapper repostedArticleMapper;

    @Transactional
    public RepostedDto createRepost(CreateOrUpdateRepostDto createOrUpdateRepostDto, Long articleId, User user){

        PublishedArticle article = publishedArticleService.getArticleEntityById(articleId);
        String comment = null;
        if(StringUtils.isNotBlank(createOrUpdateRepostDto.comment())){
            comment = createOrUpdateRepostDto.comment();
        }
        RepostedArticle repost = RepostedArticle.builder()
                .original(article)
                .author(user)
                .comment(comment)
                .build();
        return repostedArticleMapper.toDto(repostedArticleRepository.save(repost));
    }
    @Transactional(readOnly = true)
    public Page<RepostedDto> getAllRepostsByUser(Long userId, Pageable pageable){
        return repostedArticleRepository.findAllByAuthorId(userId, pageable)
                .map(repostedArticleMapper::toDto);
    }

    @Transactional(readOnly = true)
    public RepostedDto getById(Long repostId){
        RepostedArticle repost = repostedArticleRepository.findById(repostId)
                .orElseThrow(() -> new EntityNotFoundException("Article not found"));
        return repostedArticleMapper.toDto(repost);
    }

    @Transactional
    public Page<RepostedDto> getAllRepostsByOriginal(Long originalId, Pageable pageable){
        return repostedArticleRepository.findAllByOriginalId(originalId, pageable)
                .map(repostedArticleMapper::toDto);
    }

    @Transactional
    public void deleteRepost(User user, Long repostId){
        RepostedArticle repost = getRepostOrThrowException(user, repostId);

        repostedArticleRepository.delete(repost);
    }

    @Transactional
    public RepostedDto updateRepost(CreateOrUpdateRepostDto updateRepostDto, User user, Long repostId){
        RepostedArticle repost = getRepostOrThrowException(user, repostId);

        Optional.ofNullable(updateRepostDto.comment())
                .ifPresent(repost::setComment);

        return repostedArticleMapper.toDto(repostedArticleRepository.save(repost));
    }


    private RepostedArticle getRepostOrThrowException(User user, Long repostId){
        RepostedArticle repost = repostedArticleRepository.findById(repostId)
                .orElseThrow(() -> new EntityNotFoundException("Article not found"));

        if(!repost.getAuthor().getId().equals(user.getId())){
            throw new AccessDeniedException("Permission denied");
        }

        return repost;
    }

}
