package weolbu.assignment.global.security;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;

public enum PublicEndpoint {

    SIGN_UP(HttpMethod.POST, "/api/v1/members"),
    LOGIN(HttpMethod.POST, "/api/v1/members/login"),
    ;

    private static final List<PublicEndpoint> PUBLIC_ENDPOINTS = Arrays.asList(values());
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    private final HttpMethod httpMethod;
    private final String urlPattern;

    PublicEndpoint(HttpMethod httpMethod, String urlPattern) {
        this.httpMethod = httpMethod;
        this.urlPattern = urlPattern;
    }

    public static boolean isPublic(HttpServletRequest request) {
        return PUBLIC_ENDPOINTS.stream()
                .anyMatch(publicEndpoint -> publicEndpoint.matches(request));
    }

    public boolean matches(HttpServletRequest request) {
        return httpMethod.matches(request.getMethod()) && ANT_PATH_MATCHER.match(urlPattern, request.getRequestURI());
    }
}

