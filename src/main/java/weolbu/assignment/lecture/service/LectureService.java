package weolbu.assignment.lecture.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weolbu.assignment.global.dto.MemberAuth;
import weolbu.assignment.global.exception.BadRequestException;
import weolbu.assignment.lecture.domain.Lecture;
import weolbu.assignment.lecture.domain.LectureRepository;
import weolbu.assignment.lecture.domain.LectureStudent;
import weolbu.assignment.lecture.domain.LectureStudentRepository;
import weolbu.assignment.lecture.dto.LectureRequest;
import weolbu.assignment.lecture.dto.LectureResponse;
import weolbu.assignment.member.domain.Member;
import weolbu.assignment.member.domain.MemberRepository;

@RequiredArgsConstructor
@Service
public class LectureService {

    private final LectureRepository lectureRepository;
    private final MemberRepository memberRepository;
    private final LectureStudentRepository lectureStudentRepository;

    @Transactional
    public Long createLecture(LectureRequest request, MemberAuth memberAuth) {
        Member instructor = findMember(memberAuth.memberId());
        Lecture savedLecture = lectureRepository.save(request.toLecture(instructor));
        return savedLecture.getId();
    }

    @Transactional
    public void enrollLecture(Long lectureId, MemberAuth memberAuth) {
        Lecture lecture = findLecture(lectureId);
        Member student = findMember(memberAuth.memberId());
        validateAlreadyEnroll(lecture, student);
        increaseEnrollment(lecture);
        lectureStudentRepository.save(new LectureStudent(lecture, student));
    }

    @Transactional
    public Slice<LectureResponse> findLectures(Pageable pageable) {
        Slice<Lecture> lectures = lectureRepository.findAll(pageable);
        return lectures.map(LectureResponse::from);
    }

    private void validateAlreadyEnroll(Lecture lecture, Member student) {
        if (lectureStudentRepository.existsByLectureAndStudent(lecture, student)) {
            throw new BadRequestException("이미 수강 중인 강의입니다.");
        }
    }

    private void increaseEnrollment(Lecture lecture) {
        int increasedEnrollmentCount = lectureRepository.increaseEnrollment(lecture);
        if (increasedEnrollmentCount == 0) {
            throw new BadRequestException("수강 인원이 꽉 차 등록에 실패하였습니다.");
        }
    }

    private Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("사용자를 찾을 수 없습니다."));
    }

    private Lecture findLecture(Long id) {
        return lectureRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("강의를 찾을 수 없습니다."));
    }
}
