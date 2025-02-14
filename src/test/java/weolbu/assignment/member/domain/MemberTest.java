package weolbu.assignment.member.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import weolbu.assignment.global.exception.BadRequestException;

class MemberTest {

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("이름이 null이거나 빈 문자열일 경우 예외가 발생한다.")
    void nameIsNullOrEmptyExceptionTest(String name) {
        assertThatThrownBy(() ->
                new Member(name, "abc@abc.com", "010-1234-5678", new EncryptedPassword("pwd123"), MemberRole.STUDENT))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("이름은 비어있을 수 없습니다.");
    }


    @ParameterizedTest
    @MethodSource("provideLongName")
    @DisplayName("이름이 20자를 초과할 경우 예외가 발생한다.")
    void nameIsLongerThan20ExceptionTest(String name) {
        assertThatThrownBy(() ->
                new Member(name, "abc@abc.com", "010-1234-5678", new EncryptedPassword("pwd123"), MemberRole.STUDENT))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("이름의 길이는 최대 20자 입니다.");
    }

    private static List<String> provideLongName() {
        return List.of(
                "a".repeat(21),
                "b".repeat(30)
        );
    }

    @ParameterizedTest
    @MethodSource("provideShortName")
    @DisplayName("이름이 20자 이하일 경우 예외가 발생하지 않는다.")
    void nameIsShorterThan20ExceptionTest(String name) {
        assertThatCode(() ->
                new Member(name, "abc@abc.com", "010-1234-5678", new EncryptedPassword("pwd123"), MemberRole.STUDENT))
                .doesNotThrowAnyException();
    }

    private static List<String> provideShortName() {
        return List.of(
                "a",
                "b".repeat(20)
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("이메일이 null이거나 빈 문자열일 경우 예외가 발생한다.")
    void emailIsNullOrEmptyExceptionTest(String email) {
        assertThatThrownBy(() ->
                new Member("이낙헌", email, "010-1234-5678", new EncryptedPassword("pwd123"), MemberRole.STUDENT))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("이메일은 비어있을 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "abc@", "abc@abc", "abc@abc.", "abc@abc..com", "abc@@abc.com"})
    @DisplayName("이메일 형식이 올바르지 않을 경우 예외가 발생한다.")
    void emailIsInvalidExceptionTest(String email) {
        assertThatThrownBy(() ->
                new Member("이낙헌", email, "010-1234-5678", new EncryptedPassword("pwd123"), MemberRole.STUDENT))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("이메일 형식이 올바르지 않습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc@abc.com", "a@a.kr"})
    @DisplayName("이메일 형식이 올바를 경우 예외가 발생하지 않는다.")
    void emailIsValidExceptionTest(String email) {
        assertThatCode(() ->
                new Member("이낙헌", email, "010-1234-5678", new EncryptedPassword("pwd123"), MemberRole.STUDENT))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("핸드폰 번호가 null이거나 빈 문자열일 경우 예외가 발생한다.")
    void phoneNumberIsNullOrEmptyExceptionTest(String phoneNumber) {
        assertThatThrownBy(() ->
                new Member("이낙헌", "abc@abc.com", phoneNumber, new EncryptedPassword("pwd123"), MemberRole.STUDENT))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("핸드폰 번호는 비어있을 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"01012345678", "012-1234-5678", "010-12345678", "010-123-4567", "1234-4567"})
    @DisplayName("핸드폰 번호 형식이 올바르지 않을 경우 예외가 발생한다.")
    void phoneNumberIsInvalidExceptionTest(String phoneNumber) {
        assertThatThrownBy(() ->
                new Member("이낙헌", "abc@abc.com", phoneNumber, new EncryptedPassword("pwd123"), MemberRole.STUDENT))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("올바르지 않은 핸드폰 번호입니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"010-1234-5678", "010-1234-5679", "010-1234-5680"})
    @DisplayName("핸드폰 번호가 올바를 경우 예외가 발생하지 않는다.")
    void phoneNumberIsValidExceptionTest(String phoneNumber) {
        assertThatCode(() ->
                new Member("이낙헌", "abc@abc.com", phoneNumber, new EncryptedPassword("pwd123"), MemberRole.STUDENT))
                .doesNotThrowAnyException();
    }
}
