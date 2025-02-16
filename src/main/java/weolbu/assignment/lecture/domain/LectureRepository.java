package weolbu.assignment.lecture.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    @Modifying
    @Query("""
            UPDATE Lecture l
            SET l.currentEnrollment = l.currentEnrollment + 1
            WHERE l = :lecture AND l.currentEnrollment < l.enrollmentLimit
            """)
    int increaseEnrollment(Lecture lecture);
}
