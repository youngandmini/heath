package heavysnow.heath.controller;

import heavysnow.heath.dto.GoalCreationDto;
import heavysnow.heath.dto.MemberDto;
import heavysnow.heath.repository.MemberRepository;
import heavysnow.heath.service.GoalService;
import heavysnow.heath.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final GoalService goalService;

    private final MemberService memberService;

    private final MemberRepository memberRepository;

    @PatchMapping("/user/{userId}")
    public ResponseEntity<String> updateMember(@PathVariable Long userId, @RequestBody MemberDto memberDto) {
        memberService.editMember(userId, memberDto);
        return ResponseEntity.ok("Member updated successfully");
    }

    @PostMapping("members/{memberId}/goals")
    public ResponseEntity<Long> addGoal(@PathVariable Long memberId, @RequestBody GoalCreationDto goalCreationDto) {
        Long goalId = goalService.createGoalForMember(memberId, goalCreationDto);
        return ResponseEntity.ok(goalId);
    }
}
