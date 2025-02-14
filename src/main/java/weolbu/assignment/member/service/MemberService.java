package weolbu.assignment.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weolbu.assignment.global.exception.BadRequestException;
import weolbu.assignment.global.infrastructure.JwtTokenProvider;
import weolbu.assignment.member.domain.EncryptedPassword;
import weolbu.assignment.member.domain.Member;
import weolbu.assignment.member.domain.MemberRepository;
import weolbu.assignment.member.domain.RawPassword;
import weolbu.assignment.member.dto.AccessTokenResponse;
import weolbu.assignment.member.dto.LoginRequest;
import weolbu.assignment.member.dto.SignUpRequest;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AccessTokenResponse signUp(SignUpRequest signUpRequest) {
        validateDuplicate(signUpRequest);
        RawPassword rawPassword = new RawPassword(signUpRequest.password());
        EncryptedPassword encryptedPassword = rawPassword.encrypt(passwordEncoder);
        Member member = signUpRequest.toMember(encryptedPassword);
        Member savedMember = memberRepository.save(member);

        return new AccessTokenResponse(jwtTokenProvider.createAccessToken(savedMember.getId()));
    }

    private void validateDuplicate(SignUpRequest request) {
        if (memberRepository.existsByName(request.name())) {
            throw new BadRequestException("해당 이름은 중복된 이름입니다.");
        }
        if (memberRepository.existsByEmail(request.email())) {
            throw new BadRequestException("해당 이메일로 이미 가입 된 계정이 있습니다.");
        }
        if (memberRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new BadRequestException("해당 휴대폰 번호로 이미 가입 된 계정이 있습니다.");
        }
    }

    @Transactional(readOnly = true)
    public AccessTokenResponse login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new BadRequestException("올바르지 않은 ID/PW 입니다."));
        member.verifyPassword(loginRequest.password(), passwordEncoder);

        return new AccessTokenResponse(jwtTokenProvider.createAccessToken(member.getId()));
    }
}
