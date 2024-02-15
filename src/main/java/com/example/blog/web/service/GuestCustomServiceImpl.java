package com.example.blog.web.service;

import com.example.blog.web.domain.QGuest;
import com.example.blog.web.dto.GuestDto;
import com.example.blog.web.dto.QGuestDto;
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

import static com.example.blog.web.domain.QGuest.guest;


//like(str)은 쿼리가 나갈 때 str자체가 나간다
//즉, 정확히 일치해야한다
//like는 내가 % 연산을 선택할 수 있다
//contains(str)은 쿼리가 나갈 때 %str%가 나간다

@Transactional
@RequiredArgsConstructor
@Service
public class GuestCustomServiceImpl implements GuestCustomService {
    private final JPAQueryFactory jpaQueryFactory;

    //    @Override
//    public Page<GuestDto> findList(PageRequest pageRequest) throws Exception{
//        //select *, (select a.author from guest a where a.guest_no = b.cguest_no) as older_name from guest b
//        // ORDER BY
//        //    CASE
//        //        WHEN pguest_no IS NOT NULL THEN pguest_no
//        //        ELSE guest_no
//        //    END ASC;
//
//        QGuest subGuest = new QGuest("subGuest");
//        List<GuestDto> list = jpaQueryFactory.select(
//                        new QGuestDto(
//                                guest.guestNo,
//                                guest.pguestNo,
//                                guest.cguestNo,
//                                guest.author,
//                                guest.content,
//                                guest.useYn,
//                                guest.regDate,
//                                guest.updDate,
//                                guest.guestPw,
//                                ExpressionUtils.as(
//                                        JPAExpressions.select(subGuest.author).from(subGuest).where(subGuest.guestNo.eq(guest.cguestNo)),"older"
//                                )
//                        )
//                ).from(guest)
//                .where(guest.useYn.eq("Y"))
//                .orderBy(
//                        new CaseBuilder().when(guest.pguestNo.isNotNull())
//                                .then(guest.pguestNo)
//                                .otherwise(guest.guestNo).desc()
//                )
//                .offset(pageRequest.getOffset()) //페이지 번호
//                .limit(pageRequest.getPageSize()) //페이지 사이즈
//                .fetch();
//
//        Long count = jpaQueryFactory.select(guest.count())
//                .from(guest)
//                .where(guest.useYn.eq("Y"))
//                .fetchOne();
//
//        return new PageImpl<>(list, pageRequest, count);
//    }

    @Override
    public Page<GuestDto> findList(PageRequest pageRequest) throws Exception{
        //select * from guest where pguest_no is null and use_yn = 'Y';

        QGuest subGuest = new QGuest("subGuest");
        List<GuestDto> list = jpaQueryFactory.select(
                        new QGuestDto(
                                guest.guestNo,
                                guest.pguestNo,
                                guest.cguestNo,
                                guest.author,
                                guest.content,
                                guest.useYn,
                                guest.regDate,
                                guest.updDate,
                                guest.guestPw,
                                ExpressionUtils.as(
                                        JPAExpressions.select(subGuest.author).from(subGuest).where(subGuest.guestNo.eq(guest.cguestNo)),"older"
                                )
                        )
                ).from(guest)
                .where(guest.useYn.eq("Y").and(guest.pguestNo.isNull()))
                .orderBy(guest.guestNo.desc())
                .offset(pageRequest.getOffset()) //페이지 번호
                .limit(pageRequest.getPageSize()) //페이지 사이즈
                .fetch();

        Long count = jpaQueryFactory.select(guest.count())
                .from(guest)
                .where(guest.useYn.eq("Y").and(guest.pguestNo.isNull()))
                .fetchOne();

        return new PageImpl<>(list, pageRequest, count);
    }

    @Override
    public List<GuestDto> findChildrenList(List<Long> guestNo) throws Exception {
        //select * from guest where pguest_no is not null and use_yn = 'Y';
        QGuest subGuest = new QGuest("subGuest");
        List<GuestDto> list = jpaQueryFactory.select(
                        new QGuestDto(
                                guest.guestNo,
                                guest.pguestNo,
                                guest.cguestNo,
                                guest.author,
                                guest.content,
                                guest.useYn,
                                guest.regDate,
                                guest.updDate,
                                guest.guestPw,
                                ExpressionUtils.as(
                                        JPAExpressions.select(subGuest.author).from(subGuest).where(subGuest.guestNo.eq(guest.cguestNo)),"older"
                                )
                        )
                ).from(guest)
                .where(guest.useYn.eq("Y").and(guest.pguestNo.isNotNull()).and(guest.pguestNo.in(guestNo)))
                .orderBy(guest.guestNo.desc())
                .fetch();
        return list;
    }

    @Override
    public Page<GuestDto> findByContent(String content, PageRequest pageRequest) throws Exception {
        //select *, (select a.author from guest a where a.guest_no = b.cguest_no) as older_name from guest b
        //where guest_no in (select pguest_no from guest b where content like "%zz%") or content like "%zz%";
        QGuest subGuest = new QGuest("subGuest");
        List<GuestDto> list = jpaQueryFactory.select(
                        new QGuestDto(
                                guest.guestNo,
                                guest.pguestNo,
                                guest.cguestNo,
                                guest.author,
                                guest.content,
                                guest.useYn,
                                guest.regDate,
                                guest.updDate,
                                guest.guestPw,
                                ExpressionUtils.as(
                                        JPAExpressions.select(subGuest.author).from(subGuest).where(subGuest.guestNo.eq(guest.cguestNo)),"older"
                                )
                        )
                ).from(guest)
                .where(guest.guestNo.in(
                        JPAExpressions.select(
                                guest.pguestNo
                        ).from(guest).where(guest.content.contains(content))
                ).or(guest.content.contains(content)).and(guest.useYn.eq("Y")).and(guest.pguestNo.isNull()))
                .orderBy(guest.guestNo.desc())
                .offset(pageRequest.getOffset()) //페이지 번호
                .limit(pageRequest.getPageSize()) //페이지 사이즈
                .fetch();

        Long count = jpaQueryFactory.select(guest.count())
                .from(guest)
                .where(guest.guestNo.in(
                        JPAExpressions.select(
                                guest.pguestNo
                        ).from(guest).where(guest.content.contains(content))
                ).or(guest.content.contains(content)).and(guest.useYn.eq("Y")).and(guest.pguestNo.isNull()))
                .fetchOne();

        return new PageImpl<>(list, pageRequest, count);
    }

    @Override
    public Page<GuestDto> findByAuthor(String author, PageRequest pageRequest) throws Exception {
        //select *, (select a.author from guest a where a.guest_no = b.cguest_no) as older_name from guest b
        //where guest_no in (select pguest_no from guest b where author like "%zz%") or author like "%zz%";

        QGuest subGuest = new QGuest("subGuest");
        List<GuestDto> list = jpaQueryFactory.select(
                        new QGuestDto(
                                guest.guestNo,
                                guest.pguestNo,
                                guest.cguestNo,
                                guest.author,
                                guest.content,
                                guest.useYn,
                                guest.regDate,
                                guest.updDate,
                                guest.guestPw,
                                ExpressionUtils.as(
                                        JPAExpressions.select(subGuest.author).from(subGuest).where(subGuest.guestNo.eq(guest.cguestNo)),"older"
                                )
                        )
                ).from(guest)
                .where(guest.guestNo.in(
                        JPAExpressions.select(
                                guest.pguestNo
                        ).from(guest).where(guest.author.contains(author))
                ).or(guest.author.contains(author)).and(guest.useYn.eq("Y")).and(guest.pguestNo.isNull()))
                .orderBy(guest.guestNo.desc())
                .offset(pageRequest.getOffset()) //페이지 번호
                .limit(pageRequest.getPageSize()) //페이지 사이즈
                .fetch();

        Long count = jpaQueryFactory.select(guest.count())
                .from(guest)
                .where(guest.guestNo.in(
                        JPAExpressions.select(
                                guest.pguestNo
                        ).from(guest).where(guest.author.contains(author))
                ).or(guest.author.contains(author)).and(guest.useYn.eq("Y")).and(guest.pguestNo.isNull()))
                .fetchOne();

        return new PageImpl<>(list, pageRequest, count);
    }

    @Override
    public Long countByGuest(String useYn) throws Exception {
        Long countNum = jpaQueryFactory.select(guest.count())
                .from(guest)
                .where(guest.useYn.like(useYn))
                .fetchOne();

        return countNum;
    }

    //    @Override
//    public Page<GuestDto> findByContent(String content, PageRequest pageRequest) throws Exception {
//        //select *, (select a.author from guest a where a.guest_no = b.cguest_no) as older_name from guest b where b.content like %content%;
//        QGuest subGuest = new QGuest("subGuest");
//        List<GuestDto> list = jpaQueryFactory.select(
//                        new QGuestDto(
//                                guest.guestNo,
//                                guest.pguestNo,
//                                guest.cguestNo,
//                                guest.author,
//                                guest.content,
//                                guest.useYn,
//                                guest.regDate,
//                                guest.updDate,
//                                guest.guestPw,
//                                ExpressionUtils.as(
//                                        JPAExpressions.select(subGuest.author).from(subGuest).where(subGuest.guestNo.eq(guest.cguestNo)),"older"
//                                )
//                        )
//                ).from(guest)
//                .where(guest.content.contains(content))
//                .orderBy(guest.guestNo.desc())
//                .offset(pageRequest.getOffset()) //페이지 번호
//                .limit(pageRequest.getPageSize()) //페이지 사이즈
//                .fetch();
//
//        Long count = jpaQueryFactory.select(guest.count())
//                .from(guest)
//                .fetchOne();
//
//        return new PageImpl<>(list, pageRequest, count);
//    }
//
//    @Override
//    public Page<GuestDto> findByAuthor(String author, PageRequest pageRequest) throws Exception {
//        //select *, (select a.author from guest a where a.guest_no = b.cguest_no) as older_name from guest b where b.author like %author%;
//
//        QGuest subGuest = new QGuest("subGuest");
//        List<GuestDto> list = jpaQueryFactory.select(
//                        new QGuestDto(
//                                guest.guestNo,
//                                guest.pguestNo,
//                                guest.cguestNo,
//                                guest.author,
//                                guest.content,
//                                guest.useYn,
//                                guest.regDate,
//                                guest.updDate,
//                                guest.guestPw,
//                                ExpressionUtils.as(
//                                        JPAExpressions.select(subGuest.author).from(subGuest).where(subGuest.guestNo.eq(guest.cguestNo)),"older"
//                                )
//                        )
//                ).from(guest)
//                .where(guest.author.contains(author))
//                .orderBy(guest.guestNo.desc())
//                .offset(pageRequest.getOffset()) //페이지 번호
//                .limit(pageRequest.getPageSize()) //페이지 사이즈
//                .fetch();
//
//        Long count = jpaQueryFactory.select(guest.count())
//                .from(guest)
//                .fetchOne();
//
//        return new PageImpl<>(list, pageRequest, count);
//
//
//    }

}
