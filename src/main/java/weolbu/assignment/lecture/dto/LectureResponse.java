package weolbu.assignment.lecture.dto;

import java.math.BigInteger;
import lombok.Builder;
import weolbu.assignment.lecture.domain.Lecture;

@Builder
public record LectureResponse(
        Long id,
        String name,
        BigInteger price,
        String instructorName,
        int enrollmentLimit,
        int currentEnrollment
) {

    public static LectureResponse from(Lecture lecture) {
        return LectureResponse.builder()
                .id(lecture.getId())
                .name(lecture.getName())
                .price(lecture.getPrice().toBigInteger())
                .instructorName(lecture.getInstructor().getName())
                .enrollmentLimit(lecture.getEnrollmentLimit())
                .currentEnrollment(lecture.getCurrentEnrollment())
                .build();
    }
}
