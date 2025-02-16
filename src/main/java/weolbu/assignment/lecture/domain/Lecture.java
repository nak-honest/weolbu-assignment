package weolbu.assignment.lecture.domain;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.text.NumberFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import weolbu.assignment.global.exception.BadRequestException;
import weolbu.assignment.global.exception.ForbiddenException;
import weolbu.assignment.member.domain.Member;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = {@Index(name = "idx_lecture_current_enrollment", columnList = "current_enrollment")})
@Entity
public class Lecture {

    private static final int MAX_NAME_LENGTH = 100;
    private static final int MIN_ENROLLMENT_LIMIT = 1;
    private static final int MAX_ENROLLMENT_LIMIT = 1_000;
    private static final int MIN_PRICE = 0;
    private static final int MAX_PRICE = 10_000_000;
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = MAX_NAME_LENGTH)
    private String name;

    @Column(nullable = false)
    private int enrollmentLimit;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private Member instructor;

    @Column(nullable = false)
    private int currentEnrollment;

    // 특정 벤더사에 의존하지 않으면서 소수점 계산을 하기 위해 1.0을 곱해줍니다.
    @Formula("1.0 * current_enrollment / enrollment_limit")
    private double enrollmentRate;

    public Lecture(String name, int enrollmentLimit, BigDecimal price, Member instructor) {
        validate(name, enrollmentLimit, price, instructor);
        this.name = name;
        this.enrollmentLimit = enrollmentLimit;
        this.price = price;
        this.instructor = instructor;
        this.currentEnrollment = 0;
    }

    private void validate(String name, int enrollmentLimit, BigDecimal price, Member instructor) {
        validateName(name);
        validateEnrollmentLimit(enrollmentLimit);
        validatePrice(price);
        validateInstructor(instructor);
    }

    private void validateName(String name) {
        if (StringUtils.isBlank(name)) {
            throw new BadRequestException("강의명은 비어있을 수 없습니다.");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new BadRequestException(String.format("강의명의 길이는 최대 %d자 입니다.", MAX_NAME_LENGTH));
        }
    }

    private void validateEnrollmentLimit(int enrollmentLimit) {
        if (enrollmentLimit < MIN_ENROLLMENT_LIMIT || enrollmentLimit > MAX_ENROLLMENT_LIMIT) {
            throw new BadRequestException(
                    String.format("최대 수강 인원은 %s명 이상 %s명 이하여야 합니다.",
                            NUMBER_FORMAT.format(MIN_ENROLLMENT_LIMIT), NUMBER_FORMAT.format(MAX_ENROLLMENT_LIMIT)));
        }
    }

    private void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new BadRequestException("가격은 비어있을 수 없습니다.");
        }
        if (price.compareTo(BigDecimal.valueOf(MIN_PRICE)) < 0 || price.compareTo(BigDecimal.valueOf(MAX_PRICE)) > 0) {
            throw new BadRequestException(String.format("가격은 %s원 이상 %s원 이하여야 합니다.",
                    NUMBER_FORMAT.format(MIN_PRICE), NUMBER_FORMAT.format(MAX_PRICE)));
        }
    }

    private void validateInstructor(Member instructor) {
        if (instructor == null) {
            throw new IllegalArgumentException("강사는 null 일 수 없습니다.");
        }
        if (!instructor.isInstructor()) {
            throw new ForbiddenException("강사만 강의를 등록할 수 있습니다.");
        }
    }
}
