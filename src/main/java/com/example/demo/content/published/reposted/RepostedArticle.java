package com.example.demo.content.published.reposted;

import com.example.demo.content.published.article.PublishedArticle;
import com.example.demo.content.shared.ArticleVisitor;
import com.example.demo.content.shared.BaseArticle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("REPOSTED")
@Entity
public class RepostedArticle extends BaseArticle {

    @ManyToOne
    @JoinColumn(name = "original_id")
    private PublishedArticle original;

    private String comment;


    @Override
    protected <T> T accept(ArticleVisitor<T> visitor) {
        return visitor.visitRepostedArticle(this);
    }
}
