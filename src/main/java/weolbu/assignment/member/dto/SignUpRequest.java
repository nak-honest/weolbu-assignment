package weolbu.assignment.member.dto;

import weolbu.assignment.member.domain.EncryptedPassword;
import weolbu.assignment.member.domain.Member;
import weolbu.assignment.member.domain.MemberRole;

public record SignUpRequest(
        String name,
        String email,
        String phoneNumber,
        String password,
        MemberRole role
) {

    public Member toMember(EncryptedPassword encryptedPassword) {
        return new Member(name, email, phoneNumber, encryptedPassword, role);
    }
}
