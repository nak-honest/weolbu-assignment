package weolbu.assignment.member.domain;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import weolbu.assignment.global.exception.BadRequestException;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class MemberName {

    private static final int MAX_NAME_LENGTH = 20;

    @Column(nullable = false, length = MAX_NAME_LENGTH)
    private String name;

    public MemberName(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        if (StringUtils.isBlank(name)) {
            throw new BadRequestException("이름은 비어있을 수 없습니다.");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new BadRequestException(String.format("이름의 길이는 최대 %d자 입니다.", MAX_NAME_LENGTH));
        }
    }
}
