package weolbu.assignment.lecture.dto;

import java.math.BigDecimal;
import weolbu.assignment.lecture.domain.Lecture;
import weolbu.assignment.member.domain.Member;

public record LectureRequest(
        String name,
        int enrollmentLimit,
        BigDecimal price
) {

    public Lecture toLecture(Member instructor) {
        return new Lecture(name, enrollmentLimit, price, instructor);
    }
}
