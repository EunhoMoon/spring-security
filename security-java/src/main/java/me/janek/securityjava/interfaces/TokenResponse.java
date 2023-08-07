package me.janek.securityjava.interfaces;

public record TokenResponse(String userToken, String accessToken, String refreshToken) {
}
