package com.example.blog.web.controller;

import com.example.blog.web.domain.Guest;
import com.example.blog.web.dto.GuestDto;
import com.example.blog.web.service.GuestCustomService;
import com.example.blog.web.service.GuestService;
import com.example.blog.web.utils.AesUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/guests")
public class GuestController {
    private final GuestService guestService;
    private final GuestCustomService guestCustomService;
    int page = 0;
    int size = 9;

    @PostMapping
    public ResponseEntity<Page<Guest>> register(@Validated @RequestBody Guest guest) throws Exception {
        log.info("guest register");
        guest.setUseYn("Y");
        guest.setGuestPw(AesUtil.aesEncoder(guest.getGuestPw()));
        guestService.register(guest);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "guestNo"));
        Page<Guest> createdGuest = guestService.list(pageRequest);
        return new ResponseEntity<>(createdGuest, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<GuestDto>> list(@RequestParam("page") Integer page, @RequestParam("size") Integer size) throws Exception {
        log.info("guest list");
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "guestNo"));
        Page<GuestDto> list = guestCustomService.findList(pageRequest);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PutMapping("/{guestNo}")
    public ResponseEntity<Guest> modify(@PathVariable("guestNo") Long guestNo, @Validated @RequestBody Guest guest) throws Exception {
        log.info("guest modify");

        Guest guestEntity = guestService.read(guestNo);
        String decodedText = AesUtil.aesDecoder(guestEntity.getGuestPw());

        if (decodedText.equals(guest.getGuestPw())) {
            guest.setGuestNo(guestNo);
            guestService.modify(guest);
            return new ResponseEntity<>(guest, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/{guestNo}")
    public ResponseEntity<Guest> remove(@PathVariable("guestNo") Long guestNo, @Validated @RequestBody Guest guest) throws Exception {
        log.info("guest remove newenwenwenwe");
        System.out.println(guest.getGuestPw());

        Guest guestEntity = guestService.read(guestNo);
        String decodedText = AesUtil.aesDecoder(guestEntity.getGuestPw());

        if (decodedText.equals(guest.getGuestPw())) {
            guest.setGuestNo(guestNo);
            guestService.remove(guest);
            return new ResponseEntity<>(guest, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/search/author")
    public ResponseEntity<Page<GuestDto>> searchAuthor(@RequestParam("author") String author, @RequestParam("page") Integer page, @RequestParam("size") Integer size) throws Exception {
        log.info("search author");
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "guestNo"));
        Page<GuestDto> list = guestCustomService.findByAuthor(author, pageRequest);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    @GetMapping("/search/content")
    public ResponseEntity<Page<GuestDto>> searchContent(@RequestParam("content") String content, @RequestParam("page") Integer page, @RequestParam("size") Integer size) throws Exception {
        log.info("search content");
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "guestNo"));
        Page<GuestDto> list = guestCustomService.findByContent(content, pageRequest);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/children")
    public ResponseEntity<List<GuestDto>> childrenList(@RequestBody List<Long> guestNo) throws Exception {
        log.info("get children list");
        List<GuestDto> list = guestCustomService.findChildrenList(guestNo);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/count")
    public Long countGuest() throws Exception {
        log.info("count");
        Long count = guestCustomService.countByGuest("Y");
        return count;
    }
}
