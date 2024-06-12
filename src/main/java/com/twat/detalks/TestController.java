package com.twat.detalks;

import com.twat.detalks.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final MemberService memberService;

    @Autowired
    public TestController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String testApi(){
        return "test api";
    }

    @GetMapping("/rep")
    public String testApi2(){
        memberService.actionMemberReputation("1","VOTE_DOWN");
        return "test api";
    }
}
