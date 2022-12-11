package com.musinsa.demo.common.exception;

import com.musinsa.demo.dto.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class RewardExceptionHandler {
    private static final String ERROR_LOG_MSG =
            "[REQUEST (Method: %s) (URI: %s)] [ERROR (Type: %s) (Message: %s)]";

    @ExceptionHandler({ServiceNotFoundException.class})
    public ResponseEntity<CommonResponse<Void>> notFoundServiceException(
            HttpServletRequest request, ServiceNotFoundException ex
    ) {
        log.error(getExceptionLog(request, ex), ex);
        return buildErrorResponse(ex.getErrorType());
    }

    @ExceptionHandler({RewardServiceException.class})
    public ResponseEntity<CommonResponse<Void>> serviceException(
            HttpServletRequest request, RewardServiceException ex
    ) {
        log.error(getExceptionLog(request, ex), ex);
        return buildErrorResponse(ex.getErrorType());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<CommonResponse<Void>> methodArgumentNotValidException(
            HttpServletRequest request, MethodArgumentNotValidException ex) {
        log.error(getExceptionLog(request, ex), ex);

        return buildErrorResponse(ServiceErrorType.BAD_REQUEST_VALIDATION);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<CommonResponse<Void>> methodArgumentNotValidException(
            HttpServletRequest request, IllegalArgumentException ex) {
        log.error(getExceptionLog(request, ex), ex);

        return buildErrorResponse(ServiceErrorType.BAD_REQUEST_VALIDATION);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<CommonResponse<Void>> unknownException(
            HttpServletRequest request, Exception ex) {
        log.error(getExceptionLog(request, ex), ex);

        return buildErrorResponse(ServiceErrorType.UNKNOWN_SERVER_ERROR);
    }



    private ResponseEntity<CommonResponse<Void>> buildErrorResponse(ErrorType errorType) {
        return buildErrorResponse(errorType, errorType.getMessage());
    }

    private ResponseEntity<CommonResponse<Void>> buildErrorResponse(ErrorType errorCode, String errorMessage) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(CommonResponse.errorOf(errorCode.getHttpStatus(), errorMessage));
    }

    private String getExceptionLog(HttpServletRequest request, Exception exception) {
        return String.format(
                ERROR_LOG_MSG,
                request.getMethod(),
                request.getRequestURI(),
                exception.getClass().getSimpleName(),
                exception.getMessage());
    }
}
