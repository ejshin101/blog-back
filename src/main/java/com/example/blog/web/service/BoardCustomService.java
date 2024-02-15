package com.example.blog.web.service;

import com.example.blog.web.dto.BoardCountDto;
import com.example.blog.web.dto.BoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface BoardCustomService {
    public Page<BoardDto> findByTitle(String title, PageRequest pageRequest) throws Exception;
    public Page<BoardDto> findByContent(String content, PageRequest pageRequest) throws Exception;
    public Page<BoardDto> findByCategory(String category, PageRequest pageRequest) throws Exception;
    public Page<BoardDto> findByTitleWithCategory(String title, String categoryStr, PageRequest pageRequest) throws Exception;
    public Page<BoardDto> findByContentWithCategory(String content, String categoryStr, PageRequest pageRequest) throws Exception;
    public List<BoardCountDto> findCount() throws Exception;
}
