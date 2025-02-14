package weolbu.assignment.member.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import weolbu.assignment.global.exception.BadRequestException;

class MemberPhoneNumberTest {

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("핸드폰 번호가 null이거나 빈 문자열일 경우 예외가 발생한다.")
    void phoneNumberIsNullOrEmptyExceptionTest(String phoneNumber) {
        assertThatThrownBy(() -> new MemberPhoneNumber(phoneNumber))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("핸드폰 번호는 비어있을 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"01012345678", "012-1234-5678", "010-12345678", "010-123-4567", "1234-4567"})
    @DisplayName("핸드폰 번호 형식이 올바르지 않을 경우 예외가 발생한다.")
    void phoneNumberIsInvalidExceptionTest(String phoneNumber) {
        assertThatThrownBy(() -> new MemberPhoneNumber(phoneNumber))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("올바르지 않은 핸드폰 번호입니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"010-1234-5678", "010-1234-5679", "010-1234-5680"})
    @DisplayName("핸드폰 번호가 올바를 경우 예외가 발생하지 않는다.")
    void phoneNumberIsValidExceptionTest(String phoneNumber) {
        assertThatCode(() -> new MemberPhoneNumber(phoneNumber))
                .doesNotThrowAnyException();
    }
}
