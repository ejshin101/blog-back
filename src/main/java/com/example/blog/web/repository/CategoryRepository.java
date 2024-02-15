package com.example.blog.web.repository;

import com.example.blog.web.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByCategoryCdContaining(String categoryCd);
    List<Category> findByCategoryNmContaining(String categoryNm);
    List<Category> findByGroupIdContaining(String groupId);

}
