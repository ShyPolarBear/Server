package com.shy_polarbear.server.global.auth.security;



import com.shy_polarbear.server.global.auth.jwt.JwtAuthenticationEntryPoint;
import com.shy_polarbear.server.global.auth.jwt.JwtAuthenticationFilter;
import com.shy_polarbear.server.global.auth.jwt.JwtProvider;
import com.shy_polarbear.server.global.common.constants.GlobalConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtProvider jwtProvider;
    private final PrincipalDetailService principalDetailService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션 사용 안함
                .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                    .authorizeRequests()
                            .antMatchers(GlobalConstants.permittedUrls).permitAll()
                            .antMatchers(HttpMethod.POST, GlobalConstants.permittedUrlsWithPostMethod).permitAll()
                        .anyRequest().authenticated()
                .and()
                    .logout()
                    .logoutUrl("/logout") // 로그아웃 엔드포인트 설정
                .and()
                    .addFilterBefore(new JwtAuthenticationFilter(jwtProvider),
                        UsernamePasswordAuthenticationFilter.class); //JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣는다

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(principalDetailService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
