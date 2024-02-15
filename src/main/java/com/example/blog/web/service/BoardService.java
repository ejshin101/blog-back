package com.example.blog.web.service;

import com.example.blog.web.domain.Board;
import com.example.blog.web.dto.BoardCategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BoardService {
    public void register(Board board) throws Exception;
    @Transactional
    public Board read(Long boardNo) throws Exception;
    public Page<Board> list(PageRequest pageRequest) throws Exception;
    @Transactional
    public void modify(Board board) throws Exception;
    @Transactional
    public void remove(Long boardNo) throws Exception;
    public Page<Board> findByTitleContaining(String title, PageRequest pageRequest) throws Exception;
    public Page<Board> findByContentContaining(String content, PageRequest pageRequest) throws Exception;
    public Page<BoardCategoryDto> findBoardsWithCategory(PageRequest pageRequest) throws Exception;
    public List<BoardCategoryDto> findCountByTitle(String title) throws Exception;
    public List<BoardCategoryDto> findCountByContent(String content) throws Exception;
    public List<BoardCategoryDto> findCountByCategory(String category) throws Exception;

}
