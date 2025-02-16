package weolbu.assignment.lecture.contorller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import weolbu.assignment.global.dto.ErrorResponse;
import weolbu.assignment.global.dto.MemberAuth;
import weolbu.assignment.lecture.dto.LectureRequest;
import weolbu.assignment.lecture.dto.LectureResponse;

@Tag(name = "Lecture", description = "강의 API")
public interface LectureControllerDocs {

    @Operation(
            summary = "강의 개설",
            description = "강의를 개설한다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "강의 개설 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = """
                            잘못된 요청 (다음과 같은 경우 발생)
                            - 액세스 토큰의 member_id 에 해당하는 회원이 존재하지 않는 경우
                            - 강의 명이 비어있거나 100자를 초과하는 경우
                            - 강의 최대 수강 인원이 0보다 작거나 1,000을 초과하는 경우
                            - 강의 가격이 null 이거나 0보다 작거나 10,000,000을 초과하는 경우
                            """,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "유효하지 않은 액세스 토큰으로 인한 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "로그인 한 회원이 강사가 아닌 경우 발생",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<Void> createLecture(LectureRequest request, MemberAuth memberAuth);

    @Operation(
            summary = "수강 신청",
            description = "강의를 수강 신청한다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "수강 신청 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = """
                            잘못된 요청 (다음과 같은 경우 발생)
                            - lecture_id 에 해당하는 강의가 존재하지 않는 경우
                            - 액세스 토큰의 member_id 에 해당하는 회원이 존재하지 않는 경우
                            - 이미 수강 중인 강의인 경우
                            - 수강 인원이 꽉 찬 경우
                            """,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "유효하지 않은 액세스 토큰으로 인한 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<Void> enrollLecture(Long id, MemberAuth memberAuth);

    @Operation(
            summary = "강의 목록 조회",
            description = """
                    강의 목록을 조회한다.
                    정렬 조건은 다음과 같다.
                    - id : 최근 등록 순
                    - currentEnrollment : 신청자 많은 순
                    - enrollmentRate : 신청률 높은 순
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "강의 목록 조회 성공"
            )
    })
    ResponseEntity<Slice<LectureResponse>> findLectures(@ParameterObject Pageable pageable);
}
