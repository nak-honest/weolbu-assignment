package weolbu.assignment.lecture.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import weolbu.assignment.global.exception.BadRequestException;
import weolbu.assignment.global.exception.ForbiddenException;
import weolbu.assignment.member.domain.EncryptedPassword;
import weolbu.assignment.member.domain.Member;
import weolbu.assignment.member.domain.MemberRole;

class LectureTest {

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("강의명이 null이거나 빈 문자열일 경우 예외가 발생한다.")
    void nameIsNullOrEmptyExceptionTest(String name) {
        assertThatThrownBy(() -> new Lecture(name, 10, BigDecimal.valueOf(10_000), createMember(MemberRole.INSTRUCTOR)))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("강의명은 비어있을 수 없습니다.");
    }

    @ParameterizedTest
    @MethodSource("provideLongName")
    @DisplayName("강의명이 100자를 초과할 경우 예외가 발생한다.")
    void nameIsLongerThan100ExceptionTest(String name) {
        assertThatThrownBy(() -> new Lecture(name, 10, BigDecimal.valueOf(10_000), createMember(MemberRole.INSTRUCTOR)))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("강의명의 길이는 최대 100자 입니다.");
    }

    private static List<String> provideLongName() {
        return List.of(
                "a".repeat(101),
                "b".repeat(200)
        );
    }

    @ParameterizedTest
    @MethodSource("provideShortName")
    @DisplayName("강의명이 100자 이하일 경우 예외가 발생하지 않는다.")
    void nameIsShorterThan100ExceptionTest(String name) {
        assertThatCode(() -> new Lecture(name, 10, BigDecimal.valueOf(10_000), createMember(MemberRole.INSTRUCTOR)))
                .doesNotThrowAnyException();
    }

    private static List<String> provideShortName() {
        return List.of(
                "a",
                "b".repeat(100)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-2, -1, 0, 1_001, 1_002})
    @DisplayName("수강 인원이 1명 미만 또는 1,000명 초과일 경우 예외가 발생한다.")
    void enrollmentLimitIsInvalidExceptionTest(int enrollmentLimit) {
        assertThatThrownBy(() ->
                new Lecture("강의명", enrollmentLimit, BigDecimal.valueOf(10_000), createMember(MemberRole.INSTRUCTOR)))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("최대 수강 인원은 1명 이상 1,000명 이하여야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 999, 1_000})
    @DisplayName("수강 인원이 1명 이상 1,000명 이하일 경우 예외가 발생하지 않는다.")
    void enrollmentLimitIsValidExceptionTest(int enrollmentLimit) {
        assertThatCode(() ->
                new Lecture("강의명", enrollmentLimit, BigDecimal.valueOf(10_000), createMember(MemberRole.INSTRUCTOR)))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("가격이 null일 경우 예외가 발생한다.")
    void priceIsNullExceptionTest() {
        assertThatThrownBy(() -> new Lecture("강의명", 10, null, createMember(MemberRole.INSTRUCTOR)))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("가격은 비어있을 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {-2, -1, 10_000_001, 10_000_002})
    @DisplayName("가격이 0원 미만 또는 10,000,000원 초과일 경우 예외가 발생한다.")
    void priceIsInvalidExceptionTest(int price) {
        assertThatThrownBy(() ->
                new Lecture("강의명", 10, BigDecimal.valueOf(price), createMember(MemberRole.INSTRUCTOR)))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("가격은 0원 이상 10,000,000원 이하여야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 9_999_999, 10_000_000})
    @DisplayName("가격이 0원 이상 10,000,000원 이하일 경우 예외가 발생하지 않는다.")
    void priceIsValidExceptionTest(int price) {
        assertThatCode(() ->
                new Lecture("강의명", 10, BigDecimal.valueOf(price), createMember(MemberRole.INSTRUCTOR)))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("강사가 null일 경우 예외가 발생한다.")
    void instructorIsNullExceptionTest() {
        assertThatThrownBy(() -> new Lecture("강의명", 10, BigDecimal.valueOf(10_000), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("강사는 null 일 수 없습니다.");
    }

    @Test
    @DisplayName("강사가 아닌 사람이 강의를 등록하는 경우 예외가 발생한다.")
    void instructorIsNotInstructorExceptionTest() {
        assertThatThrownBy(() -> new Lecture("강의명", 10, BigDecimal.valueOf(10_000), createMember(MemberRole.STUDENT)))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("강사만 강의를 등록할 수 있습니다.");
    }

    @Test
    @DisplayName("강사가 강의를 등록하는 경우 예외가 발생하지 않는다.")
    void instructorIsInstructorExceptionTest() {
        assertThatCode(() -> new Lecture("강의명", 10, BigDecimal.valueOf(10_000), createMember(MemberRole.INSTRUCTOR)))
                .doesNotThrowAnyException();
    }

    private Member createMember(MemberRole role) {
        return new Member("이낙헌", "abc@abc.com", "010-1234-5678", new EncryptedPassword("pwd123"), role);
    }
}
