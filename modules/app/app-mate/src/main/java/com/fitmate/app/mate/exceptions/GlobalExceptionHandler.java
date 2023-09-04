package com.fitmate.app.mate.exceptions;

import com.fitmate.exceptions.exception.DuplicatedException;
import com.fitmate.exceptions.exception.LimitException;
import com.fitmate.exceptions.exception.NotFoundException;
import com.fitmate.exceptions.exception.NotMatchException;
import com.fitmate.exceptions.result.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatus status,
                                                                  final WebRequest request) {
        final List<String> errorList = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        log.warn("유효하지 않은 DTO 파라미터 에러 : {}", errorList);
        return this.makeErrorResponseEntity(errorList.toString());
    }

    private ResponseEntity<Object> makeErrorResponseEntity(final String errorDescription) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), errorDescription));
    }

    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<ErrorResponse> handleJoinDuplicatedException(final DuplicatedException exception) {
        log.warn("DuplicatedException: ", exception);
        return this.makeErrorResponseEntity(exception.getErrorResult());
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final DuplicatedErrorResult errorResult) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception exception) {
        log.warn("Unknown Exception occur: ", exception);
        return this.makeErrorResponseEntity(CommonErrorResult.UNKNOWN_EXCEPTION);
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final CommonErrorResult errorResult) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(final NotFoundException exception) {
        log.warn("존재하지 않는 데이터: ", exception);
        return this.makeErrorResponseEntity(exception.getErrorResult());
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final NotFoundErrorResult errorResult) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage()));
    }

    @ExceptionHandler(MalformedURLException.class)
    public ResponseEntity<ErrorResponse> handleMalformedURLException(final MalformedURLException exception) {
        log.warn("URL 리소스 존재 X: ", exception);
        return this.makeErrorResponseEntity(FileErrorResult.INVALID_URL_RESOURCE);
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final FileErrorResult errorResult) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage()));
    }

    @ExceptionHandler(NotMatchException.class)
    public ResponseEntity<ErrorResponse> handleNotMatchException(final NotMatchException exception) {
        log.warn("값의 매치가 맞지 않습니다: ", exception);
        return this.makeErrorResponseEntity(NotMatchErrorResult.NOT_MATCH_WAIT_ACCOUNT_LIST);
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final NotMatchErrorResult errorResult) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage()));
    }


    @ExceptionHandler(LimitException.class)
    public ResponseEntity<ErrorResponse> handleLimitException(final LimitException exception) {
        log.warn("값의 매치가 맞지 않습니다: ", exception);
        return this.makeErrorResponseEntity(LimitErrorResult.OVER_MATE_PEOPLE_LIMIT);
    }

    private ResponseEntity<ErrorResponse> makeErrorResponseEntity(final LimitErrorResult errorResult) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errorResult.name(), errorResult.getMessage()));
    }
}
