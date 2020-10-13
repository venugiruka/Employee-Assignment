package com.paypal.bfs.test.employeeserv.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.paypal.bfs.test.employeeserv.model.ErrorModel;
import com.paypal.bfs.test.employeeserv.model.ExceptionModel;
import com.paypal.bfs.test.employeeserv.util.ErrorsEnum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler
        extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value
            = { ConstraintViolationException.class, RollbackException.class})
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Invalid Request body"+ex.getLocalizedMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
    
    /**
	 * Global exception other than above 3 exceptions
	 * @param
	 *
	 * @return
	 */
	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<Object> otherExceptions(Exception exception, WebRequest webRequest) {
		log.error(ErrorsEnum.BAD_REQUEST_EXCEPTION.getMessage(), exception);
		ExceptionModel exceptionModel = ExceptionModel.builder()
				.errors(exception.getClass().getName() + " : " + exception.getMessage()).build();
		return handleExceptionInternal(exception, exceptionModel, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,
				webRequest);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorModel errorModel = ErrorModel.builder().value(ex.getBindingResult())
				.timestamp(Timestamp.valueOf(LocalDateTime.now()).getTime()).status(HttpStatus.BAD_REQUEST.value())
				.build();

		return new ResponseEntity<>(errorModel, HttpStatus.BAD_REQUEST);
	}
}