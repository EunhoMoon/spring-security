package me.janek.securityjava.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtValidationStatus {
    VALID("검증된 토큰입니다."),
    EXPIRED("만료된 토큰입니다."),
    INVALID_SIGNATURE("잘못된 서명입니다."),
    NOT_SUPPORTED("지원되지 않는 토큰입니다."),
    INVALID_TOKEN("토큰이 잘못되었습니다.");

    private final String describe;
}
