package com.example.demo.content.draft;

import com.example.demo.content.shared.ArticleMapper;
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
@DiscriminatorValue("DRAFT")
@Entity
public class DraftArticle extends BaseArticle {
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Override
    protected <T> T toDto(ArticleMapper<T> visitor) {
        return null;
    }
}
