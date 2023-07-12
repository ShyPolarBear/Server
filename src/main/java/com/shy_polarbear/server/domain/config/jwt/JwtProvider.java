package com.shy_polarbear.server.domain.config.jwt;


import com.shy_polarbear.server.domain.user.exception.AuthException;
import com.shy_polarbear.server.domain.user.model.User;
import com.shy_polarbear.server.global.exception.ExceptionStatus;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private final UserDetailsService userDetailsService;
    private final Key privateKey;


    public JwtProvider(@Value("${jwt.secret}") String secretKey,
                       RefreshTokenRepository refreshTokenRepository,
                       UserDetailsService userDetailsService) {
        this.privateKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.refreshTokenRepository = refreshTokenRepository;
        this.userDetailsService = userDetailsService;
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
    public String createSignToken() {
        return null;
    }

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

    //토큰에서 Authentication 객체 (인증용 객체) 가져오기
    public Authentication getAuthentication(String accessToken) {
        String userName = getAccessTokenPayload(accessToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getAccessTokenPayload(String accessToken) {
        return Jwts.parser()
                .setSigningKey(privateKey).parseClaimsJws(accessToken)
                .getBody().getSubject();
    }

    // 토큰 유효성 검증, 만료 일자 확인
    public boolean isValidateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(privateKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public void validateToken(String token) {
        if(!isValidateToken(token)){
            throw new AuthException(ExceptionStatus.INVALID_TOKEN);
        }
    }

    // accessToken, refreshToken 최초 발행
    public JwtDto issue(User user) {
        String accessToken = createAccessToken(user);
        String refreshToken = createRefreshToken(user);
        Optional<RefreshToken> findRefreshToken = refreshTokenRepository.findByUser(user);
        if (findRefreshToken.isPresent()) {
            findRefreshToken.get().replace(refreshToken);
        } else {
            refreshTokenRepository.save(new RefreshToken(user, refreshToken));
        }
        return JwtDto.from(accessToken, refreshToken);

    }

    // accessToken, refreshToken 재발행
    public JwtDto reissue(String refreshToken) {
        RefreshToken findRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AuthException(ExceptionStatus.INVALID_TOKEN));
        User user = findRefreshToken.getUser();

        //TODO: 유저 상태 확인
        String newAccessToken = createAccessToken(user);
        String newRefreshToken = createRefreshToken(user);

        user.updateAccessToken(newAccessToken);
        findRefreshToken.replace(newRefreshToken);
        return JwtDto.from(newAccessToken, newAccessToken);
    }

}
