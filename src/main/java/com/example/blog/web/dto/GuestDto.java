package com.example.blog.web.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import java.time.LocalDateTime;
@Getter
public class GuestDto {
    private Long guestNo;
    private Long pguestNo;
    private Long cguestNo;
    private String author;
    private String content;
    private String useYn;
    private LocalDateTime regDate;
    private LocalDateTime updDate;
    private String guestPw;
    private String older;

    @QueryProjection
    public GuestDto(Long guestNo, Long pguestNo, Long cguestNo, String author, String content, String useYn, LocalDateTime regDate, LocalDateTime updDate, String guestPw, String older) {
        this.guestNo = guestNo;
        this.pguestNo = pguestNo;
        this.cguestNo = cguestNo;
        this.author = author;
        this.content = content;
        this.useYn = useYn;
        this.regDate = regDate;
        this.updDate = updDate;
        this.guestPw = guestPw;
        this.older = older;
    }
}
