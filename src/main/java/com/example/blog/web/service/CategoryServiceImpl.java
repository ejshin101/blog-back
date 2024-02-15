package com.example.blog.web.service;

import com.example.blog.web.domain.Category;
import com.example.blog.web.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    @Override
    public void register(Category category) throws Exception {
        categoryRepository.save(category);
    }

    @Override
    public Category read(Long categoryNo) throws Exception {
        return categoryRepository.getOne(categoryNo);
    }

    @Override
    public List<Category> list() throws Exception {
        return categoryRepository.findAll(Sort.by(
                Sort.Order.asc("groupId"),
                Sort.Order.asc("categoryOrder")
        ));
    }

    @Override
    public void modify(Category category) throws Exception {
        Category categoryEntity = categoryRepository.getOne(category.getCategoryNo());

        categoryEntity.setCategoryCd(category.getCategoryCd());
        categoryEntity.setCategoryNm(category.getCategoryNm());
        categoryEntity.setCategoryOrder(category.getCategoryOrder());

        categoryRepository.save(categoryEntity);
    }

    @Override
    public void remove(long categoryNo) throws Exception {
        categoryRepository.deleteById(categoryNo);
    }

    @Override
    public List<Category> findByCategoryCdContaining(String categoryCd) throws Exception {
        return categoryRepository.findByCategoryCdContaining(categoryCd);
    }

    @Override
    public List<Category> findByCategoryNmContaining(String categoryNm) throws Exception {
        return categoryRepository.findByCategoryNmContaining(categoryNm);
    }

    @Override
    public List<Category> findByGroupIdContaining(String groupId) throws Exception {
        return categoryRepository.findByGroupIdContaining(groupId);
    }
}
