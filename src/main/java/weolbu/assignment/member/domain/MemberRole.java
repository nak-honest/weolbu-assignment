package weolbu.assignment.member.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import java.util.Objects;
import weolbu.assignment.global.exception.BadRequestException;

public enum MemberRole {

    STUDENT,
    INSTRUCTOR;

    @JsonCreator
    public static MemberRole from(String name) {
        return Arrays.stream(values())
                .filter(memberRole -> Objects.equals(memberRole.name(), name))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("존재하지 않는 회원 타입입니다."));
    }
}
