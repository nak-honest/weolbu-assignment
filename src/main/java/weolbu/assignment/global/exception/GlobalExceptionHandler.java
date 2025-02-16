package weolbu.assignment.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import weolbu.assignment.global.dto.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException exception) {
        log.warn("BAD_REQUEST_EXCEPTION :: message = {}", exception.getMessage());
        ErrorResponse data = new ErrorResponse(exception.getMessage());
        return ResponseEntity.badRequest().body(data);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException exception) {
        log.warn("UNAUTHORIZED_EXCEPTION :: message = {}", exception.getMessage());
        ErrorResponse data = new ErrorResponse(exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(data);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException exception) {
        log.warn("FORBIDDEN_EXCEPTION :: message = {}", exception.getMessage());
        ErrorResponse data = new ErrorResponse(exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(data);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error("INTERNAL_SERVER_ERROR :: exception = ", exception);
        ErrorResponse data = new ErrorResponse("서버 에러가 발생했습니다.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(data);
    }
}
