package weolbu.assignment.member.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import weolbu.assignment.global.exception.BadRequestException;

class MemberNameTest {

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("이름이 null이거나 빈 문자열일 경우 예외가 발생한다.")
    void nameIsNullOrEmptyExceptionTest(String name) {
        assertThatThrownBy(() -> new MemberName(name))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("이름은 비어있을 수 없습니다.");
    }

    @ParameterizedTest
    @MethodSource("provideLongName")
    @DisplayName("이름이 20자를 초과할 경우 예외가 발생한다.")
    void nameIsLongerThan20ExceptionTest(String name) {
        assertThatThrownBy(() -> new MemberName(name))
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
        assertThatCode(() -> new MemberName(name))
                .doesNotThrowAnyException();
    }

    private static List<String> provideShortName() {
        return List.of(
                "a",
                "b".repeat(20)
        );
    }
}
