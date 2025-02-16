package weolbu.assignment.lecture.contorller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import weolbu.assignment.global.dto.MemberAuth;
import weolbu.assignment.lecture.dto.LectureRequest;
import weolbu.assignment.lecture.dto.LectureResponse;
import weolbu.assignment.lecture.service.LectureService;

@RequestMapping("/api/v1/lectures")
@RequiredArgsConstructor
@RestController
public class LectureController implements LectureControllerDocs {

    private final LectureService lectureService;

    @PostMapping
    public ResponseEntity<Void> createLecture(@RequestBody LectureRequest request, MemberAuth memberAuth) {
        Long lectureId = lectureService.createLecture(request, memberAuth);
        return ResponseEntity.created(URI.create("/api/v1/lectures/" + lectureId))
                .build();
    }

    @PostMapping("/{id}/enroll")
    public ResponseEntity<Void> enrollLecture(@PathVariable Long id, MemberAuth memberAuth) {
        lectureService.enrollLecture(id, memberAuth);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Slice<LectureResponse>> findLectures(
            @PageableDefault(size = 20, sort = "id", direction = Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(lectureService.findLectures(pageable));
    }
}
