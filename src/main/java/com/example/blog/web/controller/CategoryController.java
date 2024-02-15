package com.example.blog.web.controller;

import com.example.blog.web.domain.Category;
import com.example.blog.web.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> register(@Validated @RequestBody Category category) throws Exception {
        log.info("category register");
        categoryService.register(category);
        log.info("register category.getCategoryNo() = " + category.getCategoryNo());
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Category>> list() throws Exception {
        log.info("category list");
        return new ResponseEntity<>(categoryService.list(), HttpStatus.OK);
    }

    @PutMapping("/{categoryNo}")
    public ResponseEntity<Category> modify(@PathVariable("categoryNo") Long categoryNo, @Validated @RequestBody Category category) throws Exception {
        log.info("category modify");
        category.setCategoryNo(categoryNo);
        categoryService.modify(category);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryNo}")
    public ResponseEntity<Void> remove(@PathVariable("categoryNo") Long categoryNo) throws Exception {
        log.info("remove");
        categoryService.remove(categoryNo);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/search/code")
    public ResponseEntity<List<Category>> searchCode(@RequestParam("code") String code) throws Exception {
        log.info("search code");
        return new ResponseEntity<>(categoryService.findByCategoryCdContaining(code), HttpStatus.OK);
    }
    @GetMapping("/search/name")
    public ResponseEntity<List<Category>> searchName(@RequestParam("name") String name) throws Exception {
        log.info("search name");
        return new ResponseEntity<>(categoryService.findByCategoryNmContaining(name), HttpStatus.OK);
    }
    @GetMapping("/search/group")
    public ResponseEntity<List<Category>> searchGroup(@RequestParam("group") String group) throws Exception {
        log.info("search name");
        return new ResponseEntity<>(categoryService.findByGroupIdContaining(group), HttpStatus.OK);
    }
}

