package weolbu.assignment.global.security;

import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;
import weolbu.assignment.global.exception.UnauthorizedException;

@Component
public class AccessTokenExtractor {

    private static final String AUTHENTICATION_TYPE = "Bearer ";

    public String extractToken(String authorizationHeader) {
        validateAuthorizationHeader(authorizationHeader);
        return authorizationHeader.substring(AUTHENTICATION_TYPE.length());
    }

    private void validateAuthorizationHeader(String authorizationHeader) {
        validateHeaderIsBlank(authorizationHeader);
        validateAuthenticationType(authorizationHeader);
    }

    private void validateHeaderIsBlank(String authorizationHeader) {
        if (StringUtils.isBlank(authorizationHeader)) {
            throw new UnauthorizedException("Authorization 헤더가 비어있습니다.");
        }
    }

    private void validateAuthenticationType(String authorizationHeader) {
        if (!authorizationHeader.startsWith(AUTHENTICATION_TYPE)) {
            throw new UnauthorizedException(String.format("인증 헤더는 %s로 시작해야 합니다.", AUTHENTICATION_TYPE));
        }
    }
}
