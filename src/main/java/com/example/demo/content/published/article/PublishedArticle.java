package com.example.demo.content.published.article;

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
@DiscriminatorValue("PUBLISHED")
@Entity
public class PublishedArticle extends BaseArticle {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Override
    protected <T> T accept(ArticleVisitor<T> visitor) {
        return visitor.visitPublishedArticle(this);
    }
}
