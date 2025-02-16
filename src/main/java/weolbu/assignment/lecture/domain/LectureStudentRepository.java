package weolbu.assignment.lecture.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import weolbu.assignment.member.domain.Member;

public interface LectureStudentRepository extends JpaRepository<LectureStudent, Long> {

    boolean existsByLectureAndStudent(Lecture lecture, Member student);
}
