package weolbu.assignment.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import weolbu.assignment.global.dto.ErrorResponse;
import weolbu.assignment.member.dto.AccessTokenResponse;
import weolbu.assignment.member.dto.LoginRequest;
import weolbu.assignment.member.dto.SignUpRequest;

@Tag(name = "Member", description = "회원 API")
public interface MemberControllerDocs {

    @Operation(
            summary = "회원 가입",
            description = "회원 가입을 한다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원 가입 성공. 액세스 토큰을 응답합니다."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = """
                            잘못된 요청 (다음과 같은 경우 발생)
                            - 회원 가입 시 이름이나 이메일, 휴대폰 번호가 중복되는 경우
                            - 비밀 번호가 6~10 자리가 아닌 경우
                            - 비밀 번호가 영문 대문자, 소문자, 숫자 중 2가지 이상을 포함하지 않는 경우
                            - 이름이 비어있거나 20자를 초과하는 경우
                            - 이메일이 비어있거나 이메일 형식이 아닌 경우
                            - 휴대폰 번호가 비어있거나 휴대폰 번호 형식이 아닌 경우
                            """,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<AccessTokenResponse> signUp(SignUpRequest signUpRequest);

    @Operation(
            summary = "로그인",
            description = "로그인을 한다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공. 액세스 토큰을 응답합니다."
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = """
                            잘못된 요청 (다음과 같은 경우 발생)
                            - 해당 이메일로 가입된 회원이 없는 경우
                            - 비밀번호가 일치하지 않는 경우
                            """,
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    ResponseEntity<AccessTokenResponse> login(LoginRequest loginRequest);
}
