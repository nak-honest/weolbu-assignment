package weolbu.assignment.member.domain;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import weolbu.assignment.global.exception.BadRequestException;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class MemberEmail {

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)+$");

    @Column(nullable = false)
    private String email;

    public MemberEmail(String email) {
        validate(email);
        this.email = email;
    }

    private void validate(String email) {
        if (StringUtils.isBlank(email)) {
            throw new BadRequestException("이메일은 비어있을 수 없습니다.");
        }
        if (!EMAIL_REGEX.matcher(email).matches()) {
            throw new BadRequestException("이메일 형식이 올바르지 않습니다.");
        }
    }
}
