package com.example.blog.web.repository;

import com.example.blog.web.domain.Board;
import com.example.blog.web.dto.BoardCategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Page<Board> findByTitleContaining(String title, PageRequest pageRequest);
    Page<Board> findByContentContaining(String content, PageRequest pageRequest);
    @Query(value = "SELECT a.board_no as boardNo, a.category as category, a.author as author, a.title as title, a.content as content, a.reg_date as regDate, a.upd_date as updDate, " +
            "b.group_id as groupId, b.category_cd as categoryCd, b.category_nm as categoryNm, b.category_order as categoryOrder " +
            "FROM board a LEFT JOIN category b ON a.category = b.category_cd", nativeQuery = true)
    Page<BoardCategoryDto> findBoardsWithCategory(PageRequest pageRequest);

    @Query(
            value = "select c.category_cd as category, c.category_nm as categoryNm, ifnull(d.count, 0) as count " +
                    "from category c " +
                    "left join " +
                    "(select a.category, count(a.category) as count, (select category_nm from category b where b.category_cd = a.category ) as category_nm " +
                    "from board a where a.title like %:title% group by a.category) d " +
                    "on d.category = c.category_cd " +
                    "where c.group_id = 'ES002'", nativeQuery = true
    )
    List<BoardCategoryDto> findCountByTitle(@Param(value = "title") String title) throws Exception;
    @Query(
            value = "select c.category_cd as category, c.category_nm as categoryNm, ifnull(d.count, 0) as count " +
                    "from category c " +
                    "left join " +
                    "(select a.category, count(a.category) as count, (select category_nm from category b where b.category_cd = a.category ) as category_nm " +
                    "from board a where a.content like %:content% group by a.category) d " +
                    "on d.category = c.category_cd " +
                    "where c.group_id = 'ES002'", nativeQuery = true
    )
    List<BoardCategoryDto> findCountByContent(@Param(value = "content") String content) throws Exception;
    @Query(
            value = "select c.category_cd as category, c.category_nm as categoryNm, ifnull(d.count, 0) as count " +
                    "from category c " +
                    "left join " +
                    "(select a.category, count(a.category) as count, (select category_nm from category b where b.category_cd = a.category ) as category_nm " +
                    "from board a where a.category = :category group by a.category) d " +
                    "on d.category = c.category_cd " +
                    "where c.group_id = 'ES002'", nativeQuery = true
    )
    List<BoardCategoryDto> findCountByCategory(@Param(value = "category") String category) throws Exception;
}
