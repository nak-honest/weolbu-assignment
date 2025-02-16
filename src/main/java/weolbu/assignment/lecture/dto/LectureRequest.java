package weolbu.assignment.lecture.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import weolbu.assignment.lecture.domain.Lecture;
import weolbu.assignment.member.domain.Member;

public record LectureRequest(
        @Schema(description = "이름", example = "주식 강의")
        String name,
        @Schema(description = "최대 수강 인원", example = "10")
        int enrollmentLimit,
        @Schema(description = "가격", example = "100000")
        BigDecimal price
) {

    public Lecture toLecture(Member instructor) {
        return new Lecture(name, enrollmentLimit, price, instructor);
    }
}
