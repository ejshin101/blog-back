package com.example.blog.web.service;

import com.example.blog.web.domain.Guest;
import com.example.blog.web.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
@Transactional
@RequiredArgsConstructor
@Service
public class GuestServiceImpl implements GuestService {
    private final GuestRepository guestRepository;

    @Override
    public void register(Guest guest) throws Exception {
        guestRepository.save(guest);
    }

    @Override
    public Guest read(Long guestNo) throws Exception {
        return guestRepository.getOne(guestNo);
    }

    @Override
    public Page<Guest> list(PageRequest pageRequest) throws Exception {
//        return guestRepository.findGuestWithAuthor(pageRequest);
        return guestRepository.findAll(pageRequest);
    }

    @Override
    public void modify(Guest guest) throws Exception {
        Guest guestEntity = guestRepository.getOne(guest.getGuestNo());

        guestEntity.setContent(guest.getContent());
        guestEntity.setUpdDate(LocalDateTime.now());

        guestRepository.save(guestEntity);
    }

    @Override
    public void remove(Guest guest) throws Exception {
        Guest guestEntity = guestRepository.getOne(guest.getGuestNo());

        guestEntity.setUseYn("N");
        guestEntity.setUpdDate(LocalDateTime.now());

        guestRepository.save(guestEntity);
    }

    @Override
    public Page<Guest> findByContentContaining(String content, PageRequest pageRequest) throws Exception {
        return guestRepository.findByContentContaining(content, pageRequest);
    }

    @Override
    public Page<Guest> findByAuthorContaining(String author, PageRequest pageRequest) throws Exception {
        return guestRepository.findByAuthorContaining(author, pageRequest);
    }
}