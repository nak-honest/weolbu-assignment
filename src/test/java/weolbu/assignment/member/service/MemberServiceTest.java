package weolbu.assignment.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import weolbu.assignment.global.exception.BadRequestException;
import weolbu.assignment.global.infrastructure.JwtTokenProvider;
import weolbu.assignment.member.domain.EncryptedPassword;
import weolbu.assignment.member.domain.Member;
import weolbu.assignment.member.domain.MemberRepository;
import weolbu.assignment.member.domain.MemberRole;
import weolbu.assignment.member.dto.SignUpRequest;
import weolbu.assignment.member.dto.SignUpResponse;

@Import({MemberService.class, JwtTokenProvider.class, BCryptPasswordEncoder.class})
@DataJpaTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("중복된 이름으로 회원가입 시 예외가 발생한다.")
    void signUpWithDuplicateNameExceptionTest() {
        // given
        Member exsistMember = saveMember();
        SignUpRequest request =
                new SignUpRequest(exsistMember.getName(), "abc@abc.com", "010-1234-5678", "pwd123", MemberRole.STUDENT);

        // when & then
        assertThatThrownBy(() -> memberService.signUp(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("해당 이름은 중복된 이름입니다.");
    }

    @Test
    @DisplayName("중복된 이메일로 회원가입 시 예외가 발생한다.")
    void signUpWithDuplicateEmailExceptionTest() {
        // given
        Member exsistMember = saveMember();
        SignUpRequest request =
                new SignUpRequest("naknak", exsistMember.getEmail(), "010-1234-5678", "pwd123", MemberRole.STUDENT);

        // when & then
        assertThatThrownBy(() -> memberService.signUp(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("해당 이메일로 이미 가입 된 계정이 있습니다.");
    }

    @Test
    @DisplayName("중복된 전화번호로 회원가입 시 예외가 발생한다.")
    void signUpWithDuplicatePhoneNumberExceptionTest() {
        // given
        Member exsistMember = saveMember();
        SignUpRequest request =
                new SignUpRequest("naknak", "abc@abc.com", exsistMember.getPhoneNumber(), "pwd123", MemberRole.STUDENT);

        // when & then
        assertThatThrownBy(() -> memberService.signUp(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("해당 휴대폰 번호로 이미 가입 된 계정이 있습니다.");
    }

    @Test
    @DisplayName("회원가입 성공 시 토큰을 반환한다.")
    void signUpTest() {
        // given
        SignUpRequest request = new SignUpRequest("naknak", "abc@abc.com", "010-1234-5678", "pwd123",
                MemberRole.STUDENT);

        // when
        SignUpResponse response = memberService.signUp(request);

        // then
        assertThat(response.accessToken()).isNotBlank();
    }

    private Member saveMember() {
        Member member = new Member(
                "weolbu", "weolbu@abc.com", "010-1111-1111", new EncryptedPassword("abc123"), MemberRole.STUDENT);
        return memberRepository.save(member);
    }
}
