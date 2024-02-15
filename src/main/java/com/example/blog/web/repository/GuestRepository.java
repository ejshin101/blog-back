package com.example.blog.web.repository;

import com.example.blog.web.domain.Guest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    //각각 content, author를 where 조건으로 사용할 때 다음과 같이 적는다.
    Page<Guest> findByContentContaining(String content, PageRequest pageRequest);
    Page<Guest> findByAuthorContaining(String author, PageRequest pageRequest);

}
