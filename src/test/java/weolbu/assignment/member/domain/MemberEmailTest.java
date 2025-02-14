package weolbu.assignment.member.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import weolbu.assignment.global.exception.BadRequestException;

class MemberEmailTest {

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("이메일이 null이거나 빈 문자열일 경우 예외가 발생한다.")
    void emailIsNullOrEmptyExceptionTest(String email) {
        assertThatThrownBy(() -> new MemberEmail(email))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("이메일은 비어있을 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "abc@", "abc@abc", "abc@abc.", "abc@abc..com", "abc@@abc.com"})
    @DisplayName("이메일 형식이 올바르지 않을 경우 예외가 발생한다.")
    void emailIsInvalidExceptionTest(String email) {
        assertThatThrownBy(() -> new MemberEmail(email))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("이메일 형식이 올바르지 않습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc@abc.com", "a@a.kr"})
    @DisplayName("이메일 형식이 올바를 경우 예외가 발생하지 않는다.")
    void emailIsValidExceptionTest(String email) {
        assertThatCode(() -> new MemberEmail(email))
                .doesNotThrowAnyException();
    }
}
