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
public class MemberPhoneNumber {

    private static final int MAX_PHONE_NUMBER_LENGTH = 13;
    private static final Pattern PHONE_NUMBER_REGEX =
            Pattern.compile("^(01[016789])-\\d{4}-\\d{4}$\n");

    @Column(nullable = false, length = MAX_PHONE_NUMBER_LENGTH)
    private String phoneNumber;

    public MemberPhoneNumber(String phoneNumber) {
        validate(phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    private void validate(String phoneNumber) {
        if (StringUtils.isBlank(phoneNumber)) {
            throw new BadRequestException("핸드폰 번호는 비어있을 수 없습니다.");
        }
        if (!PHONE_NUMBER_REGEX.matcher(phoneNumber).matches()) {
            throw new BadRequestException("올바르지 않은 핸드폰 번호입니다.");
        }
    }
}
