package com.example.blog.web.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@JsonIgnoreProperties(value = "hibernateLazyInitializer")
@Getter
@Setter
@EqualsAndHashCode(of = "guestNo")
@ToString
@Entity
@Table(name = "guest")
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guestNo;

    @Column
    private Long pguestNo;

    @Column
    private Long cguestNo;

    @Column(length = 50, nullable = false)
    private String author;

    @Lob
    private String content;

    @Column(length = 10, nullable = false)
    private String useYn;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    @CreationTimestamp
    private LocalDateTime regDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    @CreationTimestamp
    private LocalDateTime updDate;

    @Column(length = 200, nullable = false)
    private String guestPw;

}
