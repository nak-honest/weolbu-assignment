package weolbu.assignment.lecture.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import weolbu.assignment.global.dto.MemberAuth;
import weolbu.assignment.global.exception.BadRequestException;
import weolbu.assignment.lecture.domain.Lecture;
import weolbu.assignment.lecture.domain.LectureRepository;
import weolbu.assignment.lecture.dto.LectureRequest;
import weolbu.assignment.member.domain.Member;
import weolbu.assignment.member.domain.MemberRepository;

@RequiredArgsConstructor
@Service
public class LectureService {

    private final LectureRepository lectureRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createLecture(LectureRequest request, MemberAuth memberAuth) {
        Member instructor = findMember(memberAuth.memberId());
        Lecture savedLecture = lectureRepository.save(request.toLecture(instructor));
        return savedLecture.getId();
    }
    
    private Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("사용자를 찾을 수 없습니다."));
    }
}
