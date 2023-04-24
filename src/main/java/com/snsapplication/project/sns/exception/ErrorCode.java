package com.snsapplication.project.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@AllArgsConstructor
@Getter
public enum ErrorCode {

    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "이미 존재하는 아이디 입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버에서 에러가 발생했습니다."),
    USER_NOT_FOUNDED(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED,"비밀번호가 일치하지 않습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND,"작성한 글을 찾을 수 없습니다."),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED,"권한이 없는 요청입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),;

    private HttpStatus status;
    private String message;
}
