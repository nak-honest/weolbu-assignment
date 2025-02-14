package weolbu.assignment.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import weolbu.assignment.member.dto.SignUpRequest;
import weolbu.assignment.member.dto.SignUpResponse;
import weolbu.assignment.member.service.MemberService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(memberService.signUp(signUpRequest));
    }
}
