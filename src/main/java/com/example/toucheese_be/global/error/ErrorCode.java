package com.example.toucheese_be.global.error;

import com.google.api.Http;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 스튜디오 관련 에러
    INVALID_FILTER_PARAMETER(4000, HttpStatus.BAD_REQUEST, "필터링 파라미터가 유효하지 않습니다."),
    REVIEW_NOT_FOUND(4001, HttpStatus.NOT_FOUND, "해당 리뷰를 찾을 수 없습니다."),
    STUDIO_NOT_FOUND(4002, HttpStatus.NOT_FOUND, "해당 스튜디오를 찾을 수 없습니다."),
    ITEM_NOT_FOUND(4003, HttpStatus.NOT_FOUND, "해당 상품을 찾을 수 없습니다."),
    ITEM_OPTION_NOT_FOUND(4004, HttpStatus.NOT_FOUND, "해당 상품 옵션을 찾을 수 없습니다."),
    ORDER_NOT_FOUND(4005, HttpStatus.NOT_FOUND, "해당 예약 정보를 찾을 수 없습니다."),
    ORDER_ALREADY_APPROVED(4006, HttpStatus.CONFLICT, "해당 예약 정보는 이미 승인 되었습니다."),
    ORDER_ALREADY_REJECTED(4007, HttpStatus.CONFLICT, "해당 예약 정보는 이미 거절 되었습니다."),
    ORDER_ALREADY_CANCELLED(4008, HttpStatus.CONFLICT, "해당 예약 정보는 이미 취소 되었습니다."),
    ORDER_ALREADY_FINISH(4036, HttpStatus.CONFLICT, "해당 예약 정보는 이미 촬영 완료 처리되었습니다."),
    ORDER_CANT_FINISH(4037, HttpStatus.CONFLICT, "해당 예약은 촬영 완료 처리할 수 없습니다."),
    ORDER_CANNOT_BE_CANCELLED(4038, HttpStatus.CONFLICT, "해당 예약은 취소 처리할 수 없습니다."),

    // 회원 관련 예외
    SIGN_UP_PASSWORD_CHECK_NOT_MATCH(4009, HttpStatus.BAD_REQUEST, "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    SIGN_UP_DUPLICATED_EMAIL(4010, HttpStatus.BAD_REQUEST, "이미 존재하는 이메일 입니다."),
    SIGN_IN_EMAIL_NOT_FOUND(4011, HttpStatus.NOT_FOUND, "존재하지 않는 이메일입니다."),
    SIGN_IN_PASSWORD_NOT_MATCH(4012, HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(4013, HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    UPDATE_USERNAME_EMPTY(4014, HttpStatus.BAD_REQUEST, "사용자 이름이 비어있습니다."),
    AUTHENTICATION_FAILED(4015, HttpStatus.FORBIDDEN, "인증 확인에 실패했습니다."),
    UNAUTHORIZED_ACCESS(4016, HttpStatus.BAD_REQUEST, "회원님의 예약 내역이 아닙니다."),

    // 토큰 관련 예외
    REFRESH_TOKEN_NOT_EQAUL(4016, HttpStatus.UNAUTHORIZED, "Refresh Token 이 일치하지 않습니다."),
    NOT_FOUND_END_POINT(4017, HttpStatus.NOT_FOUND, "존재하지 않는 API입니다."),
    JWT_SECURITY_EXCEPTION(4018, HttpStatus.FORBIDDEN, "[SecurityException] 잘못된 토큰입니다."),
    MALFORMED_JWT_EXCEPTION(4019, HttpStatus.FORBIDDEN, "[MalformedJwtException] 잘못된 토큰입니다."),
    EXPIRED_JWT_EXCEPTION(4020, HttpStatus.FORBIDDEN, "Access Token이 만료되었습니다."),
    UNSUPPORTED_JWT_EXCEPTION(4021, HttpStatus.FORBIDDEN, "[UnsupportedJwtException] 잘못된 형식의 토큰"),
    ILLEGAL_ARGUMENT_JWT_EXCEPTION(4022, HttpStatus.FORBIDDEN, "[IllegalArgumentException] 잘못된 형식의 토큰"),
    TOKEN_VALIDATION_FAIL(4033, HttpStatus.FORBIDDEN, "[토큰 검증 오류]"),
    EXTRACT_EMAIL_FROM_TOKEN_FAIL(4034, HttpStatus.FORBIDDEN, "accessToken에서 email 추출 실패"),
    FCM_SERVICE_UNAVAILABLE(4035, HttpStatus.NOT_FOUND, "FCM 서비스 통신에 실패하였습니다."),

    UPDATE_DUPLICATED_NICKNAME(4036, HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),

    // 서버 관련 에러
    INTERNAL_SERVER_ERROR(5000, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    DATABASE_ERROR(5001, HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다."),
    EXTERNAL_API_ERROR(5002, HttpStatus.INTERNAL_SERVER_ERROR, "외부 API 호출 중 오류가 발생했습니다.");


    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}
