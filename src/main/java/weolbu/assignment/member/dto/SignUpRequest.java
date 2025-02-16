package weolbu.assignment.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import weolbu.assignment.member.domain.EncryptedPassword;
import weolbu.assignment.member.domain.Member;
import weolbu.assignment.member.domain.MemberRole;

public record SignUpRequest(
        @Schema(description = "이름", example = "이낙헌")
        String name,
        @Schema(description = "이메일", example = "nakheon@abc.com")
        String email,
        @Schema(description = "휴대폰 번호", example = "010-1234-5678")
        String phoneNumber,
        @Schema(description = "비밀번호", example = "abc123")
        String password,
        @Schema(description = "회원 권한", example = "STUDENT or INSTRUCTOR")
        MemberRole role
) {

    public Member toMember(EncryptedPassword encryptedPassword) {
        return new Member(name, email, phoneNumber, encryptedPassword, role);
    }
}
