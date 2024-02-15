package com.example.blog.web.service;

import com.example.blog.web.domain.Category;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CategoryService {
    public void register(Category category) throws Exception;
    public Category read(Long categoryNo) throws Exception;
    public List<Category> list() throws Exception;
    @Transactional
    public void modify(Category category) throws Exception;
    @Transactional
    public void remove(long categoryNo) throws Exception;
    public List<Category> findByCategoryCdContaining(String categoryCd) throws Exception;
    public List<Category> findByCategoryNmContaining(String categoryNm) throws Exception;
    public List<Category> findByGroupIdContaining(String groupId) throws Exception;
}
