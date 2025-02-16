package weolbu.assignment.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequest(
        @Schema(description = "이메일", example = "nakheon@abc.com")
        String email,
        @Schema(description = "비밀번호", example = "abc123")
        String password
) {
}
