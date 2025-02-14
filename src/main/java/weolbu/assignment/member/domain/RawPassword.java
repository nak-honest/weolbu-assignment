package weolbu.assignment.member.domain;

import java.util.List;
import java.util.function.IntPredicate;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;
import weolbu.assignment.global.exception.BadRequestException;

@Getter
public class RawPassword {

    private static final int MIN_LENGTH = 6;
    private static final int MAX_LENGTH = 10;
    private static final int MIN_COMBINATION_RULE_MATCH_COUNT = 2;
    private static final List<IntPredicate> PASSWORD_COMBINATION_RULE =
            List.of(Character::isDigit, Character::isLowerCase, Character::isUpperCase);

    private final String password;

    public RawPassword(String password) {
        validate(password);
        this.password = password;
    }

    private void validate(String password) {
        validateLength(password);
        validateCombinationRuleMatchCount(password);
    }

    private static void validateLength(String password) {
        if (password.length() < MIN_LENGTH || password.length() > MAX_LENGTH) {
            throw new BadRequestException(String.format("비밀번호는 %d자 이상 %d자 이하여야 합니다.", MIN_LENGTH, MAX_LENGTH));
        }
    }

    private void validateCombinationRuleMatchCount(String password) {
        if (getRuleMatchCount(password) < MIN_COMBINATION_RULE_MATCH_COUNT) {
            throw new BadRequestException("비밀번호는 영문 소문자, 대문자, 숫자 중 최소 두 가지 이상이 조합 되어야 합니다.");
        }
    }

    private int getRuleMatchCount(String password) {
        long matchCount = PASSWORD_COMBINATION_RULE.stream()
                .filter(rule -> password.chars().anyMatch(rule))
                .count();
        return Math.toIntExact(matchCount);
    }

    public EncryptedPassword encrypt(PasswordEncoder passwordEncoder) {
        return new EncryptedPassword(passwordEncoder.encode(password));
    }
}
