package weolbu.assignment.member.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import weolbu.assignment.global.exception.BadRequestException;

class RawPasswordTest {

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("비밀번호가 null이거나 빈 문자열일 경우 예외가 발생한다.")
    void passwordIsNullOrEmptyExceptionTest(String password) {
        assertThatThrownBy(() -> new RawPassword(password))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("비밀번호는 6자 이상 10자 이하여야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"1a", "1234a", "1234567890a"})
    @DisplayName("비밀번호의 길이가 6자 미만 또는 10자 초과일 경우 예외가 발생한다.")
    void passwordIsInvalidLengthExceptionTest(String password) {
        assertThatThrownBy(() -> new RawPassword(password))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("비밀번호는 6자 이상 10자 이하여야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"123456", "abcdef", "ABCDEF", "비밀번호123", "abcdef!"})
    @DisplayName("비밀번호가 영문 소문자, 대문자, 숫자 중 최소 두 가지 이상 조합되지 않을 경우 예외가 발생한다.")
    void passwordIsInvalidCombinationRuleExceptionTest(String password) {
        assertThatThrownBy(() -> new RawPassword(password))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("비밀번호는 영문 소문자, 대문자, 숫자 중 최소 두 가지 이상이 조합 되어야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc123", "123abc", "a1b2c3", "ABC123", "123ABC",
            "A1B2C3", "abcABC", "ABCabc", "aAbBcC", "aA1bB2", "123456789a"})
    @DisplayName("올바른 비밀번호일 경우 예외가 발생하지 않는다.")
    void passwordIsValidCombinationRuleExceptionTest(String password) {
        assertThatCode(() -> new RawPassword(password))
                .doesNotThrowAnyException();
    }
}
