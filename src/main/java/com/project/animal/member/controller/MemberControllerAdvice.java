package com.project.animal.member.controller;

import com.project.animal.global.common.dto.ResponseDto;
import com.project.animal.member.exception.InvalidTokenException;
import com.project.animal.member.exception.NestedEmailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailSendException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice(assignableTypes = {MemberController.class})
public class MemberControllerAdvice {

    /**
     * API : (GET) /v1/api/auth/email
     * 이메일 인증 토큰 발급 과정에서 이메일 형식이 잘못된 경우, 해당 예외 발생
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MailSendException.class)
    public ResponseDto<String> mailParseException2(MailSendException e) {
        log.error("이메일 파싱 에러 발생");

        return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), null, "이메일 발송에 실패하였습니다.");
    }

    /**
     * API : (POST) /v1/api/auth/email 및 (POST) /v1/api/auth/signup
     * 1. 이메일 인증 토큰 발급 과정에서 이미 가입된 이메일의 경우, 해당 예외 발생
     * 2. 회원가입 과정에서 이미 등록된 이메일의 경우, 해당 예외 발생 (극히 드뭄)
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NestedEmailException.class)
    public ResponseDto<String> nestedEmailException(NestedEmailException e) {
        log.error("이메일 중복 에러 발생!", e);

        return new ResponseDto<>(HttpStatus.CONFLICT.value(), null, e.getMessage());
    }

    /**
     * API : (POST) /v1/api/auth/email
     * - 이메일 인증 확인 과정에서 인증번호가 잘못되었거나 만료된 경우, 해당 예외 발생
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseDto<String> invalidTokenException(InvalidTokenException e) {
        log.error("이메일 인증 확인 에러 발생", e);

        return new ResponseDto(HttpStatus.BAD_REQUEST.value(), null, e.getMessage());
    }

    /**
     * API : (POST) /v1/api/auth/signup
     * - 회원가입 과정에서 Bean Validation로 인한 데이터 검증 실패 시, 해당 예외 발생
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDto<String> test(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        Map<String, String> errorData = new HashMap<>();

        fieldErrors.stream().forEach(x -> {
            errorData.put(x.getField(), x.getDefaultMessage());
        });

        return new ResponseDto(HttpStatus.BAD_REQUEST.value(), errorData, "");
    }
}
