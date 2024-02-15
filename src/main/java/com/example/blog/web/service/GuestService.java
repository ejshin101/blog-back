package com.example.blog.web.service;

import com.example.blog.web.domain.Guest;
import com.example.blog.web.dto.GuestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

public interface GuestService {
    public void register(Guest guest) throws Exception;
    @Transactional
    public Guest read(Long guestNo) throws Exception;
    public Page<Guest> list(PageRequest pageRequest) throws Exception;
    @Transactional
    public void modify(Guest guest) throws Exception;
    @Transactional
    public void remove(Guest guest) throws Exception;
    public Page<Guest> findByContentContaining(String content, PageRequest pageRequest) throws Exception;
    public Page<Guest> findByAuthorContaining(String author, PageRequest pageRequest) throws Exception;

}
