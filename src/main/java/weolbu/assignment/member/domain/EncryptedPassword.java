package weolbu.assignment.member.domain;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class EncryptedPassword {

    @Column(nullable = false)
    private String password;

    public EncryptedPassword(String password) {
        validate(password);
        this.password = password;
    }

    private void validate(String password) {
        if (StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("암호화 된 비밀번호는 비어있을 수 없습니다.");
        }
    }

    public void verifyPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(rawPassword, password)) {
            throw new IllegalArgumentException("올바르지 않은 ID/PW 입니다.");
        }
    }
}
