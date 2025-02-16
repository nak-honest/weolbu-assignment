package weolbu.assignment.lecture.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import weolbu.assignment.global.dto.MemberAuth;
import weolbu.assignment.global.exception.BadRequestException;
import weolbu.assignment.lecture.domain.Lecture;
import weolbu.assignment.lecture.domain.LectureRepository;
import weolbu.assignment.lecture.dto.LectureRequest;
import weolbu.assignment.member.domain.EncryptedPassword;
import weolbu.assignment.member.domain.Member;
import weolbu.assignment.member.domain.MemberRepository;
import weolbu.assignment.member.domain.MemberRole;
import weolbu.util.DatabaseCleaner;
import weolbu.util.TestConfig;

@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import({LectureService.class, TestConfig.class})
@DataJpaTest
class LectureServiceTest {

    @Autowired
    LectureService lectureService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    LectureRepository lectureRepository;
    @Autowired
    DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp() {
        databaseCleaner.clear();
    }

    @Test
    @DisplayName("강의 생성 성공 테스트")
    void createLectureTest() {
        // given
        Member instructor = saveInstructor();
        LectureRequest request = new LectureRequest("주식 강의", 100, BigDecimal.valueOf(10_000));
        MemberAuth memberAuth = new MemberAuth(instructor.getId());

        // when
        Long lectureId = lectureService.createLecture(request, memberAuth);

        // then
        assertThat(lectureId).isEqualTo(1L);
    }

    @Test
    @DisplayName("강의 수강 신청 성공 테스트")
    void enrollLectureSuccessTest() {
        // given
        Member instructor = saveInstructor();
        Member student = saveStudent();
        Lecture lecture = saveLecture(instructor);

        // when
        lectureService.enrollLecture(lecture.getId(), new MemberAuth(student.getId()));

        // then
        Lecture enrolledLecture = lectureRepository.findById(lecture.getId()).get();
        assertThat(enrolledLecture.getCurrentEnrollment()).isEqualTo(1);
    }

    @Test
    @DisplayName("이미 수강 중인 강의를 수강 신청하면 예외가 발생한다.")
    void enrollLectureWithAlreadyEnrollExceptionTest() {
        // given
        Member instructor = saveInstructor();
        Member student = saveStudent();
        Lecture lecture = saveLecture(instructor);
        lectureService.enrollLecture(lecture.getId(), new MemberAuth(student.getId()));

        // when, then
        assertThatThrownBy(() -> lectureService.enrollLecture(lecture.getId(), new MemberAuth(student.getId())))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("이미 수강 중인 강의입니다.");
    }

    @Test
    @DisplayName("수강 인원이 꽉찬 강의를 수강 신청하면 예외가 발생한다.")
    void enrollLectureWithFullEnrollmentExceptionTest() {
        // given
        Member instructor = saveInstructor();
        Member student1 = saveStudent("student1", "010-2222-2222");
        Member student2 = saveStudent("student2", "010-3333-3333");
        Lecture lecture = saveLecture(instructor, 1);
        lectureService.enrollLecture(lecture.getId(), new MemberAuth(student1.getId()));

        // when, then
        assertThatThrownBy(() -> lectureService.enrollLecture(lecture.getId(), new MemberAuth(student2.getId())))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("수강 인원이 꽉 차 등록에 실패하였습니다.");
    }

    @Test
    @DisplayName("동시에 여러 사용자가 수강 신청을 하더라도 수강 인원이 초과되지 않는다.")
    void enrollLectureWithConcurrentTest() throws InterruptedException {
        // given
        Member instructor = saveInstructor();
        Lecture lecture = saveLecture(instructor, 5);
        List<Member> students = saveStudents(20);
        ExecutorService executorService = Executors.newFixedThreadPool(20);

        // when
        for (int i = 0; i < 20; i++) {
            Member student = students.get(i);
            executorService.execute(
                    () -> lectureService.enrollLecture(lecture.getId(), new MemberAuth(student.getId())));
        }

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        // then
        Lecture fullEnrollmentLecture = lectureRepository.findById(lecture.getId()).get();
        assertThat(fullEnrollmentLecture.getCurrentEnrollment()).isEqualTo(5);
    }

    private Member saveInstructor() {
        Member member = new Member(
                "weolbu", "weolbu@abc.com", "010-1111-1111", new EncryptedPassword("abc123"), MemberRole.INSTRUCTOR);
        return memberRepository.save(member);
    }

    private Member saveStudent() {
        Member member = new Member(
                "student", "student@abc.com", "010-2222-2222", new EncryptedPassword("abcABC"), MemberRole.STUDENT);
        return memberRepository.save(member);
    }

    private Member saveStudent(String name, String phoneNumber) {
        Member member = new Member(
                name, name + "@abc.com", phoneNumber, new EncryptedPassword("abcABC"), MemberRole.STUDENT);
        return memberRepository.save(member);
    }

    private List<Member> saveStudents(int count) {
        return Stream.iterate(1, i -> i + 1)
                .limit(count)
                .map(i -> saveStudent("student" + i, "010-1111-" + (1000 + i)))
                .toList();
    }

    private Lecture saveLecture(Member instructor) {
        Lecture lecture = new Lecture("주식 강의", 100, BigDecimal.valueOf(10_000), instructor);
        return lectureRepository.save(lecture);
    }

    private Lecture saveLecture(Member instructor, int enrollmentLimit) {
        Lecture lecture = new Lecture("주식 강의", enrollmentLimit, BigDecimal.valueOf(10_000), instructor);
        return lectureRepository.save(lecture);
    }
}
