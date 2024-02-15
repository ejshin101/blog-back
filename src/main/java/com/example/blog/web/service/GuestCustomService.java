package com.example.blog.web.service;

import com.example.blog.web.domain.Guest;
import com.example.blog.web.dto.GuestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface GuestCustomService {
   public Page<GuestDto> findList(PageRequest pageRequest) throws Exception;
   public List<GuestDto> findChildrenList(List<Long> guestNo) throws Exception;
   public Page<GuestDto> findByContent(String content, PageRequest pageRequest) throws Exception;
   public Page<GuestDto> findByAuthor(String author, PageRequest pageRequest) throws Exception;
   public Long countByGuest(String useYn) throws Exception;

}
