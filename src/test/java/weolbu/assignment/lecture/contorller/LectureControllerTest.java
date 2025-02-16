package weolbu.assignment.lecture.contorller;

import io.restassured.RestAssured;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import weolbu.assignment.global.security.JwtTokenProvider;
import weolbu.assignment.lecture.domain.Lecture;
import weolbu.assignment.lecture.domain.LectureRepository;
import weolbu.assignment.lecture.dto.LectureRequest;
import weolbu.assignment.member.domain.EncryptedPassword;
import weolbu.assignment.member.domain.Member;
import weolbu.assignment.member.domain.MemberRepository;
import weolbu.assignment.member.domain.MemberRole;
import weolbu.util.DatabaseCleaner;
import weolbu.util.TestConfig;

@Import(TestConfig.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class LectureControllerTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    LectureRepository lectureRepository;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    DatabaseCleaner databaseCleaner;
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseCleaner.clear();
    }

    @Test
    @DisplayName("강의를 생성한다.")
    void createLectureTest() {
        // given
        Member member = saveInstructor();
        LectureRequest request = new LectureRequest("주식 강의", 100, BigDecimal.valueOf(10_000));

        // when & then
        RestAssured.given().log().all()
                .header("Authorization", "Bearer " + jwtTokenProvider.createAccessToken(member.getId()))
                .contentType("application/json")
                .body(request)
                .when().post("/api/v1/lectures")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/api/v1/lectures/1");
    }

    @Test
    @DisplayName("강의 수강 신청한다.")
    void enrollLectureTest() {
        // given
        Member instructor = saveInstructor();
        Member student = saveStudent();
        Lecture lecture = saveLecture(instructor);

        // when & then
        RestAssured.given().log().all()
                .header("Authorization", "Bearer " + jwtTokenProvider.createAccessToken(student.getId()))
                .when().post("/api/v1/lectures/" + lecture.getId() + "/enroll")
                .then().log().all()
                .statusCode(204);
    }

    private Member saveInstructor() {
        Member member = new Member(
                "weolbu", "weolbu@abc.com", "010-1111-1111", new EncryptedPassword("abc123"), MemberRole.INSTRUCTOR);
        return memberRepository.save(member);
    }

    private Member saveStudent() {
        Member member = new Member(
                "student", "student@abc.com", "010-2222-2222", new EncryptedPassword("abc123"), MemberRole.STUDENT);
        return memberRepository.save(member);
    }

    private Lecture saveLecture(Member instructor) {
        Lecture lecture = new Lecture("주식 강의", 100, BigDecimal.valueOf(10_000), instructor);
        return lectureRepository.save(lecture);
    }
}
