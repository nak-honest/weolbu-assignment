package weolbu.assignment.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MemberName name;

    @Embedded
    private MemberEmail email;

    @Embedded
    private MemberPhoneNumber phoneNumber;

    @Embedded
    private EncryptedPassword password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    public Member(String name, String email, String phoneNumber, EncryptedPassword password, MemberRole role) {
        this.name = new MemberName(name);
        this.email = new MemberEmail(email);
        this.phoneNumber = new MemberPhoneNumber(phoneNumber);
        this.password = Objects.requireNonNull(password);
        this.role = Objects.requireNonNull(role);
    }
}
