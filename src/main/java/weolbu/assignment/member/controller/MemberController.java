package weolbu.assignment.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import weolbu.assignment.member.dto.AccessTokenResponse;
import weolbu.assignment.member.dto.LoginRequest;
import weolbu.assignment.member.dto.SignUpRequest;
import weolbu.assignment.member.service.MemberService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<AccessTokenResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(memberService.signUp(signUpRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(memberService.login(loginRequest));
    }
}
