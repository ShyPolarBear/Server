package com.shy_polarbear.server.domain.auth.service;


import java.util.Optional;

public interface RefreshTokenService {

    public void save(Long userId, String refreshToken);

    //토큰 userId로 찾기
    public Optional<String> findByUserId(Long userId);

    //토큰 refresh token으로 찾기
    public Optional<String> findByRefreshToken(String refreshToken);


    //토큰 삭제
    public void deleteTokenByUserId(Long userId);

}
