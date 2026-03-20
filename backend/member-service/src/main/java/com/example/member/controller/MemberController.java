package com.example.member.controller;

import com.example.member.model.MemberEntity;
import com.example.member.repo.MemberRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/member")
public class MemberController {
    private final MemberRepository memberRepository;

    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @PostMapping
    public MemberEntity create(@RequestBody MemberEntity member) {
        return memberRepository.save(member);
    }

    @GetMapping("/{id}")
    public Optional<MemberEntity> detail(@PathVariable String id) {
        return memberRepository.findById(id);
    }
}
