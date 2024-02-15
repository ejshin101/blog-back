package com.example.blog.web.service;

import com.example.blog.web.domain.Board;
import com.example.blog.web.dto.BoardCategoryDto;
import com.example.blog.web.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Transactional
@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService{
    private final BoardRepository boardRepository;

    @Override
    public void register(Board board) throws Exception {
        boardRepository.save(board);
    }

    @Override
    public Board read(Long boardNo) throws Exception {
        return boardRepository.getOne(boardNo);
    }

    @Override
    public Page<Board> list(PageRequest pageRequest) throws Exception {
        return boardRepository.findAll(pageRequest);
    }

    @Override
    public void modify(Board board) throws Exception {
        Board boardEntity = boardRepository.getOne(board.getBoardNo());

        boardEntity.setTitle(board.getTitle());
        boardEntity.setContent(board.getContent());
        boardEntity.setUpdDate(LocalDateTime.now());

        boardRepository.save(boardEntity);
    }

    @Override
    public void remove(Long boardNo) throws Exception {
        boardRepository.deleteById(boardNo);
    }

    @Override
    public Page<Board> findByTitleContaining(String title, PageRequest pageRequest) throws Exception {
        return boardRepository.findByTitleContaining(title, pageRequest);
    }

    @Override
    public Page<Board> findByContentContaining(String content, PageRequest pageRequest) throws Exception {
        return boardRepository.findByContentContaining(content, pageRequest);
    }

    @Override
    public Page<BoardCategoryDto> findBoardsWithCategory(PageRequest pageRequest) {
        return boardRepository.findBoardsWithCategory(pageRequest);
    }

    @Override
    public List<BoardCategoryDto> findCountByTitle(String title) throws Exception {
        return boardRepository.findCountByTitle(title);
    }

    @Override
    public List<BoardCategoryDto> findCountByContent(String content) throws Exception {
        return boardRepository.findCountByContent(content);
    }

    @Override
    public List<BoardCategoryDto> findCountByCategory(String category) throws Exception {
        return boardRepository.findCountByCategory(category);
    }
}
