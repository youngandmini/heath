package heavysnow.heath.controller;

import heavysnow.heath.dto.MemberDto;
import heavysnow.heath.repository.MemberRepository;
import heavysnow.heath.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final MemberRepository memberRepository;

    @PostMapping("/user/{userId}")
    public ResponseEntity<String> updateMember(@PathVariable Long userId, @RequestBody MemberDto memberDto) {
        memberService.editMember(userId, memberDto);
        return ResponseEntity.ok("Member updated successfully");
    }
}
