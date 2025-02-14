package weolbu.assignment.member.domain;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import weolbu.assignment.global.exception.BadRequestException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

    private static final int MAX_NAME_LENGTH = 20;
    private static final int MAX_PHONE_NUMBER_LENGTH = 13;
    private static final Pattern PHONE_NUMBER_REGEX =
            Pattern.compile("^(01[016789])-\\d{4}-\\d{4}$");
    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)+$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = MAX_NAME_LENGTH)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = MAX_PHONE_NUMBER_LENGTH)
    private String phoneNumber;

    @Embedded
    private EncryptedPassword password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    public Member(String name, String email, String phoneNumber, EncryptedPassword password, MemberRole role) {
        validate(name, email, phoneNumber);
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = Objects.requireNonNull(password);
        this.role = Objects.requireNonNull(role);
    }

    private void validate(String name, String email, String phoneNumber) {
        validateName(name);
        validateEmail(email);
        validatePhoneNumber(phoneNumber);
    }

    private void validateName(String name) {
        validateNotBlank(name, "이름은 비어있을 수 없습니다.");
        if (name.length() > MAX_NAME_LENGTH) {
            throw new BadRequestException(String.format("이름의 길이는 최대 %d자 입니다.", MAX_NAME_LENGTH));
        }
    }

    private void validateEmail(String email) {
        validateNotBlank(email, "이메일은 비어있을 수 없습니다.");
        if (!EMAIL_REGEX.matcher(email).matches()) {
            throw new BadRequestException("이메일 형식이 올바르지 않습니다.");
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        validateNotBlank(phoneNumber, "핸드폰 번호는 비어있을 수 없습니다.");
        if (!PHONE_NUMBER_REGEX.matcher(phoneNumber).matches()) {
            throw new BadRequestException("올바르지 않은 핸드폰 번호입니다.");
        }
    }

    private void validateNotBlank(String value, String message) {
        if (StringUtils.isBlank(value)) {
            throw new BadRequestException(message);
        }
    }

    public void verifyPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        password.verifyPassword(rawPassword, passwordEncoder);
    }
}
