package weolbu.assignment.lecture.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import weolbu.assignment.global.dto.MemberAuth;
import weolbu.assignment.lecture.dto.LectureRequest;
import weolbu.assignment.member.domain.EncryptedPassword;
import weolbu.assignment.member.domain.Member;
import weolbu.assignment.member.domain.MemberRepository;
import weolbu.assignment.member.domain.MemberRole;

@Import({LectureService.class})
@DataJpaTest
class LectureServiceTest {

    @Autowired
    LectureService lectureService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("강의 생성 성공 테스트")
    void createLectureTest() {
        // given
        Member member = saveMember();
        LectureRequest request = new LectureRequest("주식 강의", 100, BigDecimal.valueOf(10_000));
        MemberAuth memberAuth = new MemberAuth(member.getId());

        // when
        Long lectureId = lectureService.createLecture(request, memberAuth);

        // then
        assertThat(lectureId).isEqualTo(1L);
    }

    private Member saveMember() {
        Member member = new Member(
                "weolbu", "weolbu@abc.com", "010-1111-1111", new EncryptedPassword("abc123"), MemberRole.INSTRUCTOR);
        return memberRepository.save(member);
    }
}
