package com.example.blog.web.service;

import com.example.blog.web.domain.QCategory;
import com.example.blog.web.dto.BoardCountDto;
import com.example.blog.web.dto.BoardDto;
import com.example.blog.web.dto.QBoardCountDto;
import com.example.blog.web.dto.QBoardDto;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.blog.web.domain.QBoard.board;
import static com.example.blog.web.domain.QCategory.category;
import static com.example.blog.web.domain.QGuest.guest;

@Transactional
@RequiredArgsConstructor
@Service
public class BoardCustomServiceImpl implements BoardCustomService{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<BoardDto> findByTitle(String title, PageRequest pageRequest) throws Exception {
        //select a.board_no, a.category, a.author, a.title,
        //a.content, a.reg_date, a.upd_date, b.group_id, b.category_cd, b.category_nm, b.category_order
        //  from board a left outer join category b on a.category = b.category_cd  where title like "%?%";

        List<BoardDto> list = jpaQueryFactory.select(
                new QBoardDto(
                        board.boardNo,
                        board.category,
                        board.author,
                        board.title,
                        board.content,
                        board.regDate,
                        board.updDate,
                        category.groupId,
                        category.categoryCd,
                        category.categoryNm,
                        category.categoryOrder
                )
                ).from(board)
                .leftJoin(category)
                .on(board.category.eq(category.categoryCd))
                .where(board.title.contains(title))
                .orderBy(board.boardNo.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        Long count = jpaQueryFactory.select(board.count())
                .from(board)
                .leftJoin(category)
                .on(board.category.eq(category.categoryCd))
                .where(board.title.contains(title))
                .fetchOne();


        return new PageImpl<>(list, pageRequest, count);
    }

    @Override
    public Page<BoardDto> findByContent(String content, PageRequest pageRequest) throws Exception {
        List<BoardDto> list = jpaQueryFactory.select(
                        new QBoardDto(
                                board.boardNo,
                                board.category,
                                board.author,
                                board.title,
                                board.content,
                                board.regDate,
                                board.updDate,
                                category.groupId,
                                category.categoryCd,
                                category.categoryNm,
                                category.categoryOrder
                        )
                ).from(board)
                .leftJoin(category)
                .on(board.category.eq(category.categoryCd))
                .where(board.content.contains(content))
                .orderBy(board.boardNo.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        Long count = jpaQueryFactory.select(board.count())
                .from(board)
                .leftJoin(category)
                .on(board.category.eq(category.categoryCd))
                .where(board.content.contains(content))
                .fetchOne();


        return new PageImpl<>(list, pageRequest, count);
    }

    @Override
    public Page<BoardDto> findByCategory(String categoryCd, PageRequest pageRequest) throws Exception {
        List<BoardDto> list = jpaQueryFactory.select(
                        new QBoardDto(
                                board.boardNo,
                                board.category,
                                board.author,
                                board.title,
                                board.content,
                                board.regDate,
                                board.updDate,
                                category.groupId,
                                category.categoryCd,
                                category.categoryNm,
                                category.categoryOrder
                        )
                ).from(board)
                .leftJoin(category)
                .on(board.category.eq(category.categoryCd))
                .where(board.category.contains(categoryCd))
                .orderBy(board.boardNo.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        Long count = jpaQueryFactory.select(board.count())
                .from(board)
                .leftJoin(category)
                .on(board.category.eq(category.categoryCd))
                .where(board.category.contains(categoryCd))
                .fetchOne();


        return new PageImpl<>(list, pageRequest, count);
    }

    @Override
    public Page<BoardDto> findByTitleWithCategory(String title, String categoryStr, PageRequest pageRequest) throws Exception {
        //select a.board_no, a.category, a.author, a.title,
        //a.content, a.reg_date, a.upd_date, b.group_id, b.category_cd, b.category_nm, b.category_order
        //from board a left outer join category b on a.category = b.category_cd  where content like "%ㅋ%" and category="B002"
        List<BoardDto> list = jpaQueryFactory.select(
                        new QBoardDto(
                                board.boardNo,
                                board.category,
                                board.author,
                                board.title,
                                board.content,
                                board.regDate,
                                board.updDate,
                                category.groupId,
                                category.categoryCd,
                                category.categoryNm,
                                category.categoryOrder
                        )
                ).from(board)
                .leftJoin(category)
                .on(board.category.eq(category.categoryCd))
                .where(board.content.contains(title).and(board.category.like(categoryStr)))
                .orderBy(board.boardNo.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        Long count = jpaQueryFactory.select(board.count())
                .from(board)
                .leftJoin(category)
                .on(board.category.eq(category.categoryCd))
                .where(board.content.contains(title).and(board.category.like(categoryStr)))
                .fetchOne();


        return new PageImpl<>(list, pageRequest, count);
    }

    @Override
    public Page<BoardDto> findByContentWithCategory(String content, String categoryStr, PageRequest pageRequest) throws Exception {
        List<BoardDto> list = jpaQueryFactory.select(
                        new QBoardDto(
                                board.boardNo,
                                board.category,
                                board.author,
                                board.title,
                                board.content,
                                board.regDate,
                                board.updDate,
                                category.groupId,
                                category.categoryCd,
                                category.categoryNm,
                                category.categoryOrder
                        )
                ).from(board)
                .leftJoin(category)
                .on(board.category.eq(category.categoryCd))
                .where(board.content.contains(content).and(board.category.like(categoryStr)))
                .orderBy(board.boardNo.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        Long count = jpaQueryFactory.select(board.count())
                .from(board)
                .leftJoin(category)
                .on(board.category.eq(category.categoryCd))
                .where(board.content.contains(content).and(board.category.like(categoryStr)))
                .fetchOne();


        return new PageImpl<>(list, pageRequest, count);
    }

    @Override
    public List<BoardCountDto> findCount() throws Exception {
        //select a.category, count(a.category) as count, (select category_nm from category b where b.category_cd = a.category ) as category_nm
        // from board a group by category;
        QCategory subCategory = new QCategory("subCategory");
        List<BoardCountDto> list = jpaQueryFactory.select(
                        new QBoardCountDto(
                                board.category,
                                ExpressionUtils.as(
                                        JPAExpressions.select(subCategory.categoryNm).from(subCategory).where(subCategory.categoryCd.eq(board.category)),"categoryNm"
                                ),
                                board.count().as("count")
                        )
                ).from(board)
                .groupBy(board.category)
                .fetch();
        return list;
    }

//    @Override
//    public List<BoardCountDto> findCountByTitle(String title) throws Exception {
//        //select a.category, count(a.category) as count, (select category_nm from category b where b.category_cd = a.category ) as category_nm
//        // from board a where title like "%?%" group by category;
//        QCategory subCategory = new QCategory("subCategory");
//        List<BoardCountDto> list = jpaQueryFactory.select(
//                        new QBoardCountDto(
//                                board.category,
//                                ExpressionUtils.as(
//                                        JPAExpressions.select(subCategory.categoryNm).from(subCategory).where(subCategory.categoryCd.eq(board.category)),"categoryNm"
//                                ),
//                                board.count().as("count")
//                        )
//                ).from(board)
//                .where(board.title.contains(title))
//                .groupBy(board.category)
//                .fetch();
//        return list;
//    }
//
//    @Override
//    public List<BoardCountDto> findCountByContent(String content) throws Exception {
//        QCategory subCategory = new QCategory("subCategory");
//        List<BoardCountDto> list = jpaQueryFactory.select(
//                        new QBoardCountDto(
//                                board.category,
//                                ExpressionUtils.as(
//                                        JPAExpressions.select(subCategory.categoryNm).from(subCategory).where(subCategory.categoryCd.eq(board.category)),"categoryNm"
//                                ),
//                                board.count().as("count")
//                        )
//                ).from(board)
//                .where(board.content.contains(content))
//                .groupBy(board.category)
//                .fetch();
//        return list;
//    }
//
//    @Override
//    public List<BoardCountDto> findCountByCategory(String category) throws Exception {
//        QCategory subCategory = new QCategory("subCategory");
//        List<BoardCountDto> list = jpaQueryFactory.select(
//                        new QBoardCountDto(
//                                board.category,
//                                ExpressionUtils.as(
//                                        JPAExpressions.select(subCategory.categoryNm).from(subCategory).where(subCategory.categoryCd.eq(board.category)),"categoryNm"
//                                ),
//                                board.count().as("count")
//                        )
//                ).from(board)
//                .where(board.category.contains(category))
//                .groupBy(board.category)
//                .fetch();
//        return list;
//    }
}
