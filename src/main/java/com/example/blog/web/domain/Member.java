package com.example.blog.web.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(value = "hibernateLazyInitializer")
@Getter
@Setter
@EqualsAndHashCode(of = "userNo")
@ToString
@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_no")
    private Long userNo;

    @Column(length = 50)
    private String userId;

    @Column(length = 200)
    private String userPw;

    @NotBlank
    @Column(length = 100)
    private String userName;

    @Column(length = 3)
    private String job;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    @CreationTimestamp
    private LocalDateTime regDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    @UpdateTimestamp
    private LocalDateTime updDate;

    @Column(length = 100)
    private String email;

    @Column(length = 10)
    private String socialType;

    @Column(length = 100)
    private String socialId;

    @Column(length = 50)
    private String nickname;

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name = "user_no")
    private List<MemberAuth> authList = new ArrayList<MemberAuth>();

    public void addAuth(MemberAuth auth) {
        authList.add(auth);
    }

    public void clearAuthList() {
        authList.clear();
    }

    public Member update(String nickname){
        this.nickname = nickname;
        return this;
    }
}
