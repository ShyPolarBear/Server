package com.shy_polarbear.server.domain.auth.infrastructure;

import com.shy_polarbear.server.domain.auth.exception.AuthException;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final String secretKey;

    @Value("${jwt.token.secret-key}")
    private String secretKeyOfAccessToken;
    @Value("${jwt.token.secret-key}")
    private String secretKeyOfRefreshToken;
    @Value("${jwt.access-token.expire-length}")
    private long accessTokenValidTime;
    @Value("${jwt.refresh-token.expire-length}")
    private long refreshTokenValidTime;


    // Sign Token 생성

    // access token 생성
    public String createAccessToken(User user) {
        if (isValidateToken(user.getAccessToken(), secretKeyOfAccessToken)) {
            return user.getAccessToken();
        }

        Claims claims = Jwts.claims().setSubject(user.getId().toString());

        String accessToken = createJwt(claims, accessTokenValidTime, secretKeyOfAccessToken);
        user.updateAccessToken(accessToken);
        return accessToken;
    }

    // refresh token 생성
    public String createRefreshToken(User user) {
        if (isValidateToken(user.getRefreshToken(), secretKeyOfRefreshToken)) {
            return user.getRefreshToken();
        }

        Claims claims = Jwts.claims().setSubject(user.getId().toString());

        String refreshToken = createJwt(claims, refreshTokenValidTime, secretKeyOfRefreshToken);
        user.updateRefreshToken(refreshToken);
        return refreshToken;
    }



    private String createJwt(Claims claims, long tokenValidTime, String secretKey) {
        Date now = new Date(System.currentTimeMillis());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    // 토큰 유효성 검증
    private boolean isValidateToken(String token, String secretKey) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public void validateToken(String token) {
        if (!isValidateToken(token, secretKeyOfAccessToken)) {
            throw new AuthException(ExceptionStatus.INVALID_TOKEN);
        }
    }

    public String getAccessTokenPayload(String accessToken) {
        return Jwts.parser()
                .setSigningKey(secretKeyOfAccessToken).parseClaimsJws(accessToken)
                .getBody().getSubject();
    }
}
