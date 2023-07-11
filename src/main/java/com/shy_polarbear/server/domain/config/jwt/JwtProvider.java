package com.shy_polarbear.server.domain.config.jwt;

import com.shy_polarbear.server.domain.user.dto.TokenResponse;
import com.shy_polarbear.server.domain.user.exception.AuthException;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.security.Key;
import java.util.Date;
import java.util.Optional;


@Component
@Transactional
public class JwtProvider {

    private final RefreshTokenRepository refreshTokenRepository;
    private final Key privateKey;


    public JwtProvider(@Value("${jwt.secret}") String secretKey,
                       RefreshTokenRepository refreshTokenRepository) {
        this.privateKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.refreshTokenRepository = refreshTokenRepository;
    }
    @Value("${jwt.access-token.expire-length}")
    private long accessTokenValidTime;
    @Value("${jwt.refresh-token.expire-length}")
    private long refreshTokenValidTime;

    // http 헤더로부터 bearer 토큰을 가져옴
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


    // Sign Token 생성

    // access token 생성
    public String createAccessToken(User user) {
        Date now = new Date(System.currentTimeMillis());
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(now)
                .claim("tokenType", "access")
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, privateKey)
                .compact();
    }

    // refresh token 생성
    public String createRefreshToken(User user) {
        Date now = new Date(System.currentTimeMillis());
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(now)
                .claim("tokenType", "refresh")
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, privateKey)
                .compact();
    }

    // 토큰 유효성 검증
    public boolean isValidateToken(String token) {
        try {
            Jwts.parser().setSigningKey(privateKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getAccessTokenPayload(String accessToken) {
        return Jwts.parser()
                .setSigningKey(privateKey).parseClaimsJws(accessToken)
                .getBody().getSubject();
    }

    public void validateToken(String token) {
        if(!isValidateToken(token)){
            throw new AuthException(ExceptionStatus.INVALID_TOKEN);
        }
    }

    public TokenResponse issue(User user) {
        String accessToken = createAccessToken(user);
        String refreshToken = createRefreshToken(user);
        Optional<RefreshToken> findRefreshToken = refreshTokenRepository.findByUser(user);
        if (findRefreshToken.isPresent()) {
            findRefreshToken.get().replace(refreshToken);
        } else {
            refreshTokenRepository.save(new RefreshToken(user, refreshToken));
        }
        return TokenResponse.from(accessToken, refreshToken);

    }
}
