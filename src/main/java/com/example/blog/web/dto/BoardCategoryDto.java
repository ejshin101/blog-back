package com.example.blog.web.dto;

import java.time.LocalDateTime;

public interface BoardCategoryDto {
    //native query alias를 사용할 경우 getter 형식으로 작성
    Long getBoardNo();
    String getCategory();
    String getAuthor();
    String getTitle();
    String getContent();
    LocalDateTime getRegDate();
    LocalDateTime getUpdDate();
    String getGroupId();
    String getCategoryCd();
    String getCategoryNm();
    Long getCategoryOrder();
    Long getCount();

}
