package me.janek.securityjava.interfaces;

public record TokenRefreshRequest(String userToken, String refreshToken) {
}
