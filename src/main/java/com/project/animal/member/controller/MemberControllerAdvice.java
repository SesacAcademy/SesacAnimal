package com.project.animal.member.controller;

import com.project.animal.global.common.dto.ResponseDto;
import com.project.animal.member.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailSendException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.project.animal.global.common.constant.TokenTypeValue.USER_EMAIL;
import static com.project.animal.global.common.constant.TokenTypeValue.USER_NICKNAME;

@Slf4j
@RestControllerAdvice(assignableTypes = {MemberController.class, LoginController.class})
public class MemberControllerAdvice {

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MailSendException.class)
    public ResponseDto<String> mailParseException(MailSendException e) {
        log.error("이메일 파싱 에러 발생");

        return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), null, "이메일 발송에 실패하였습니다.");
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NestedEmailException.class)
    public ResponseDto<String> nestedEmailException(NestedEmailException e) {
        log.error("이메일 중복 에러 발생!", e);

        return new ResponseDto<>(HttpStatus.CONFLICT.value(), USER_EMAIL, e.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NestedNicknameException.class)
    public ResponseDto<String> nestedNicknameException(NestedNicknameException e) {
        log.error("닉네임 중복 에러 발생!", e);

        return new ResponseDto<>(HttpStatus.CONFLICT.value(), USER_NICKNAME, e.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(InvalidCodeException.class)
    public ResponseDto<String> invalidCodeException(InvalidCodeException e) {
        log.error("이메일 또는 문자 인증 확인 에러 발생", e);

        return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), null, e.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseDto<String> sqlIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        log.error("DB Unique 제약조건 위배 되었음");

        return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), null, "회원가입에 실패하였습니다.");
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDto<Map<String, String>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        Map<String, String> errorData = new HashMap<>();

        fieldErrors.forEach(x -> {
            errorData.put(x.getField(), x.getDefaultMessage());
        });

        return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), errorData, "");
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(LoginException.class)
    public ResponseDto<String> loginException(LoginException e) {
        log.error(e.getMessage());

        return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), null, "아이디 또는 비밀번호가 틀렸습니다.");
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NotFoundException.class)
    public ResponseDto<String> NotFoundException(NotFoundException e) {
        log.error(e.getMessage());

        return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), null, e.getMessage());
    }
}
