package com.example.blog.web.controller;

import com.example.blog.web.common.security.domain.CustomUser;
import com.example.blog.web.domain.Board;
import com.example.blog.web.dto.BoardCategoryDto;
import com.example.blog.web.dto.BoardCountDto;
import com.example.blog.web.dto.BoardDto;
import com.example.blog.web.service.BoardCustomService;
import com.example.blog.web.service.BoardService;
import com.example.blog.web.service.S3FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;
    private final BoardCustomService boardCustomService;
//    private final WebComponent ckUpload;
    private final S3FileUploadService s3FileUploadService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<Board> register(@Validated @RequestBody Board board, @AuthenticationPrincipal CustomUser customUser) throws Exception {
        String userId = customUser.getUserId();
        log.info("register userId = " + userId);
        board.setAuthor(userId);
        System.out.println(board);
        boardService.register(board);
        log.info("register board.getBoardNo() = " + board.getBoardNo());
        Board createdBoard = boardService.read(board.getBoardNo());
        return new ResponseEntity<>(createdBoard, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<BoardCategoryDto>> list(@RequestParam("page") Integer page, @RequestParam("size") Integer size) throws Exception {
        log.info("list");
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "boardNo"));
        return new ResponseEntity<>(boardService.findBoardsWithCategory(pageRequest), HttpStatus.OK);
    }

    @GetMapping("/{boardNo}")
    public ResponseEntity<Board> read(@PathVariable("boardNo") Long boardNo) throws Exception {
        log.info("read newenwenwenw");

        Board board = boardService.read(boardNo);

        return new ResponseEntity<>(board, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER') and principal.username == #author")
    @PutMapping("/{boardNo}")
    public ResponseEntity<Board> modify(@PathVariable("boardNo") Long boardNo, @Validated @RequestBody Board board) throws Exception{
        log.info("modify");
        board.setBoardNo(boardNo);
        boardService.modify(board);
        return new ResponseEntity<>(board, HttpStatus.OK);
    }

    @PreAuthorize("(hasRole('ROLE_USER') and principal.username == #author) or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{boardNo}")
    public ResponseEntity<Void> remove(@PathVariable("boardNo") Long boardNo, @RequestParam("author") String author) throws Exception{
        log.info("remove");
        log.info("remove author " + author);
        boardService.remove(boardNo);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
//    @PostMapping(value = "/image/upload")
//    public ModelAndView image(MultipartHttpServletRequest request) throws Exception {
//        // ckeditor는 이미지 업로드 후 이미지 표시하기 위해 uploaded 와 url을 json 형식으로 받아야 함
//        // modelandview를 사용하여 json 형식으로 보내기위해 모델앤뷰 생성자 매개변수로 jsonView 라고 써줌
//        // jsonView 라고 쓴다고 무조건 json 형식으로 가는건 아니고 @Configuration 어노테이션을 단
//        // WebConfig 파일에 MappingJackson2JsonView 객체를 리턴하는 jsonView 매서드를 만들어서 bean으로 등록해야 함
//        ModelAndView modelAndView = new ModelAndView("jsonView");
//        String uploadPath = ckUpload.ckUpload(request);
//
//        // uploaded, url 값을 modelandview로 보냄
//        modelAndView.addObject("uploaded", true);
//        modelAndView.addObject("url", uploadPath);
//        return modelAndView;
//    }

    @PostMapping("/image/upload")
    public Map<String, Object> uploadImage(MultipartRequest request) {
        Map<String, Object> responseData = new HashMap<>();
        try {
            //aws s3에 저장 후 이미지 url 반환 받기
            String imageUrl = s3FileUploadService.imageUpload(request);
            System.out.println(imageUrl);
            responseData.put("uploaded", true);
            responseData.put("url", imageUrl);
            return responseData;
        } catch (Exception e) {
            responseData.put("uploaded", false);
            return responseData;
        }
    }

    @GetMapping("/search/title")
    public ResponseEntity<Page<BoardDto>> searchTitle(@RequestParam("title") String title, @RequestParam("page") Integer page, @RequestParam("size") Integer size) throws Exception {
        log.info("search title");
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "boardNo"));
            return new ResponseEntity<>(boardCustomService.findByTitle(title, pageRequest), HttpStatus.OK);
    }
    @GetMapping("/search/content")
    public ResponseEntity<Page<BoardDto>> searchContent(@RequestParam("content") String content, @RequestParam("page") Integer page, @RequestParam("size") Integer size) throws Exception {
        log.info("search content");
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "boardNo"));
            return new ResponseEntity<>(boardCustomService.findByContent(content, pageRequest), HttpStatus.OK);
    }
    @GetMapping("/search/category")
    public ResponseEntity<Page<BoardDto>> searchCategory(@RequestParam("category") String category, @RequestParam("page") Integer page, @RequestParam("size") Integer size) throws Exception {
        log.info("search category");
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "boardNo"));
        return new ResponseEntity<>(boardCustomService.findByCategory(category, pageRequest), HttpStatus.OK);
    }

    @GetMapping("/search/title/category")
    public ResponseEntity<Page<BoardDto>> searchTitleWithCategory(@RequestParam("title") String title, @RequestParam("category") String categoryStr, @RequestParam("page") Integer page, @RequestParam("size") Integer size) throws Exception {
        log.info("search title");
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "boardNo"));
            return new ResponseEntity<>(boardCustomService.findByTitleWithCategory(title, categoryStr, pageRequest), HttpStatus.OK);
    }
    @GetMapping("/search/content/category")
    public ResponseEntity<Page<BoardDto>> searchContentWithCategory(@RequestParam("content") String content, @RequestParam("category") String categoryStr, @RequestParam("page") Integer page, @RequestParam("size") Integer size) throws Exception {
        log.info("search content");
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "boardNo"));
        return new ResponseEntity<>(boardCustomService.findByContentWithCategory(content, categoryStr, pageRequest), HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<List<BoardCountDto>> searchCount() throws Exception {
        log.info("search count");
        return new ResponseEntity<>(boardCustomService.findCount(), HttpStatus.OK);
    }
    @GetMapping("/count/title")
    public ResponseEntity<List<BoardCategoryDto>> searchCountTitle(@RequestParam("title") String title) throws Exception {
        log.info("search count title");
        return new ResponseEntity<>(boardService.findCountByTitle(title), HttpStatus.OK);
    }
    @GetMapping("/count/content")
    public ResponseEntity<List<BoardCategoryDto>> searchCountContent(@RequestParam("content") String content) throws Exception {
        log.info("search count content");
        return new ResponseEntity<>(boardService.findCountByContent(content), HttpStatus.OK);
    }

    @GetMapping("/count/category")
    public ResponseEntity<List<BoardCategoryDto>> searchCountCategory(@RequestParam("category") String category) throws Exception {
        log.info("search count category");
        return new ResponseEntity<>(boardService.findCountByCategory(category), HttpStatus.OK);
    }


}
