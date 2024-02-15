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
@EqualsAndHashCode(of = "codeNo")
@ToString
@Entity
@Table(name = "code")
public class Code {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codeNo;

    @Column(length = 50, nullable = false)
    private String codeCd;

    @Column(length = 50, nullable = false)
    private String codeNm;

    @Column(length = 50, nullable = false)
    private String codeOrder;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    @CreationTimestamp
    private LocalDateTime regDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    @CreationTimestamp
    private LocalDateTime updDate;
}
