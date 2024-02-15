package com.example.blog.web.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class BoardCountDto {
    private String category;
    private String categoryNm;
    private Long count;

    @QueryProjection
    public BoardCountDto(String category, String categoryNm, Long count) {
        this.category = category;
        this.categoryNm = categoryNm;
        this.count = count;
    }
}
