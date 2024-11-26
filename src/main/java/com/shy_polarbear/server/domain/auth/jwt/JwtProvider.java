package com.shy_polarbear.server.domain.auth.jwt;


import com.shy_polarbear.server.domain.auth.entity.RedisRefreshToken;
import com.shy_polarbear.server.domain.auth.service.RefreshTokenService;
import com.shy_polarbear.server.global.auth.security.PrincipalDetailService;
import com.shy_polarbear.server.domain.auth.exception.AuthException;
import com.shy_polarbear.server.domain.user.entity.User;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.security.Key;
import java.util.Date;


@Component
@Transactional
@Slf4j
public class JwtProvider {

    private final RefreshTokenService refreshTokenService;
    private final PrincipalDetailService principalDetailService;
    private final Key privateKey;

    public JwtProvider(@Value("${jwt.secret}") String secretKey,
                       RefreshTokenService refreshTokenService,
                       PrincipalDetailService principalDetailService) {
        this.privateKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.refreshTokenService = refreshTokenService;
        this.principalDetailService = principalDetailService;
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

    // access token 생성
    public String createAccessToken(String providerId) {
        Date now = new Date(System.currentTimeMillis());
        return Jwts.builder()
                .setSubject(providerId)
                .setIssuedAt(now)
                .claim("tokenType", "access")
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, privateKey)
                .compact();
    }

    // refresh token 생성
    public String createRefreshToken(String providerId) {
        Date now = new Date(System.currentTimeMillis());
        return Jwts.builder()
                .setSubject(providerId)
                .setIssuedAt(now)
                .claim("tokenType", "refresh")
                .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, privateKey)
                .compact();
    }



    // 토큰 유효성 검증, 만료 일자 확인
    public boolean isValidateAccessToken(String accessToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(privateKey).parseClaimsJws(accessToken);
            if (!claims.getBody().getExpiration().before(new Date()) && claims.getBody().get("tokenType").equals("access"))
                return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.warn("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 입니다.");
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT 입니다.");
        } catch (IllegalArgumentException e) {
            log.warn("JWT 잘못 되었습니다.");
        }
        return false;
    }

    public boolean isValidateRefreshToken(String refreshToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(privateKey).parseClaimsJws(refreshToken);
            if (!claims.getBody().getExpiration().before(new Date()) && claims.getBody().get("tokenType").equals("refresh"))
                return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.warn("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 입니다.");
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT 입니다.");
        } catch (IllegalArgumentException e) {
            log.warn("JWT 잘못 되었습니다.");
        }
        return false;
    }

    // accessToken, refreshToken 최초 발행
    public JwtDto issue(User user) {
        String accessToken = createAccessToken(user.getProviderId());
        String refreshToken = createRefreshToken(user.getProviderId());
        refreshTokenService.save(RedisRefreshToken.of(user.getId(), refreshToken));
        return JwtDto.from(accessToken, refreshToken);
    }

    //access token 재발급할 때 refreah token도 함께 재발급
    public JwtDto reissue(String refreshToken) {
        if (!isValidateRefreshToken(refreshToken)) {
            throw new AuthException(ExceptionStatus.INVALID_REFRESH_TOKEN);
        }
        String providerId = getTokenPayload(refreshToken);

        RedisRefreshToken findRefreshToken = refreshTokenService.findByUserRefreshToken(refreshToken)
                .orElseThrow(() -> new AuthException(ExceptionStatus.INVALID_REFRESH_TOKEN));

        String newAccessToken = createAccessToken(providerId);
        String newRefreshToken = createRefreshToken(providerId);
        refreshTokenService.save(RedisRefreshToken.of(findRefreshToken.getUserId(), newRefreshToken));
        return JwtDto.from(newAccessToken, newRefreshToken);
    }

    public Authentication getAuthentication(String accessToken) {
        String providerId = getTokenPayload(accessToken);
        UserDetails userDetails = principalDetailService.loadUserByUsername(providerId);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getTokenPayload(String token) {
        return Jwts.parser()
                .setSigningKey(privateKey).parseClaimsJws(token)
                .getBody().getSubject();
    }
}
