package com.musinsa.demo.common.exception;

import com.musinsa.demo.dto.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class RewardExceptionHandler {
    private static final String ERROR_LOG_MSG =
            "[REQUEST (Method: %s) (URI: %s)] [ERROR (Type: %s) (Message: %s)]";

    @ExceptionHandler({RewardServiceException.class})
    public ResponseEntity<CommonResponse<Void>> serviceException(
            HttpServletRequest request, RewardServiceException ex
    ) {
        log.error(getExceptionLog(request, ex), ex);
        return buildErrorResponse(ex.getErrorCode());
    }

    private ResponseEntity<CommonResponse<Void>> buildErrorResponse(RewardErrorCode errorCode) {
        return buildErrorResponse(errorCode, errorCode.getMessage());
    }

    private ResponseEntity<CommonResponse<Void>> buildErrorResponse(RewardErrorCode errorCode, String errorMessage) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.errorOf(errorCode.getCode(), errorMessage));
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
