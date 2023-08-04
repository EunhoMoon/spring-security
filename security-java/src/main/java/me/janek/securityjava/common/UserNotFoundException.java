package me.janek.securityjava.common;

public class UserNotFoundException extends RuntimeException {

    private final static String MESSAGE = "해당하는 회원정보를 찾을 수 없습니다.";

    public UserNotFoundException() {
        super(MESSAGE);
    }

}
