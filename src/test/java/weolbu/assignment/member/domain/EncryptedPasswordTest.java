package weolbu.assignment.member.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class EncryptedPasswordTest {

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("암호화된 비밀번호가 null이거나 빈 문자열일 경우 예외가 발생한다.")
    void encryptedPasswordIsNullOrEmptyExceptionTest(String encryptedPassword) {
        assertThatThrownBy(() -> new EncryptedPassword(encryptedPassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("암호화 된 비밀번호는 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("암호화된 비밀번호가 비어있지 않을 경우 예외가 발생하지 않는다.")
    void encryptedPasswordIsNotEmptyExceptionTest() {
        assertThatCode(() -> new EncryptedPassword("encryptedPassword"))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("비밀번호 검증 시 일치하지 않는 경우 예외가 발생한다.")
    void verifyPasswordNotMatchExceptionTest() {
        // given
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        RawPassword rawPassword = new RawPassword("rawPwd");
        EncryptedPassword encryptedPassword = rawPassword.encrypt(passwordEncoder);

        // when & then
        assertThatThrownBy(() -> encryptedPassword.verifyPassword("notMatchPw", passwordEncoder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올바르지 않은 ID/PW 입니다.");
    }

    @Test
    @DisplayName("비밀번호 검증 시 일치하는 경우 예외가 발생하지 않는다.")
    void verifyPasswordMatchExceptionTest() {
        // given
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        RawPassword rawPassword = new RawPassword("rawPwd");
        EncryptedPassword encryptedPassword = rawPassword.encrypt(passwordEncoder);

        // when & then
        assertThatCode(() -> encryptedPassword.verifyPassword("rawPwd", passwordEncoder))
                .doesNotThrowAnyException();
    }
}
