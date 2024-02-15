package com.example.blog.web.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardDto {
    private Long boardNo;
    private String category;
    private String author;
    private String title;
    private String content;
    private LocalDateTime regDate;
    private LocalDateTime updDate;
    private String groupId;
    private String categoryCd;
    private String categoryNm;
    private Long categoryOrder;

    @QueryProjection
    public BoardDto(Long boardNo, String category, String author, String title, String content, LocalDateTime regDate, LocalDateTime updDate, String groupId, String categoryCd, String categoryNm, Long categoryOrder) {
        this.boardNo = boardNo;
        this.category = category;
        this.author = author;
        this.title = title;
        this.content = content;
        this.regDate = regDate;
        this.updDate = updDate;
        this.groupId = groupId;
        this.categoryCd = categoryCd;
        this.categoryNm = categoryNm;
        this.categoryOrder = categoryOrder;
    }
}
