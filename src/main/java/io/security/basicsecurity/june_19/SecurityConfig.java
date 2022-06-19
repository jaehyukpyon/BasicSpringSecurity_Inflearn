package io.security.basicsecurity.june_19;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/nologin", "/nologin2").permitAll()
                .antMatchers("/**/favicon.ico").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successHandler(new AuthenticationSuccessHandler() { // AuthenticationSuccessHandler interface를 구현한 구현체를 설정하면 됨
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        // Authentication -> 인증에 성공했을 때 그 결과를 담은 인증 객체까지 파리미터로 넘어옴
                        System.out.println("authentication - authentication.getName(): " + authentication.getName()); // authentication - authentication.getName(): user
                        response.sendRedirect("/");
                        System.out.println("이거 실행 되나요?"); // 잘 되네요...
                    }
                })
                .and()
                .sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true); // 추후에 로그인 시도하는 사용자를 차단하는 설정. 디폴트는 false로, 기존 사용자의 세션을 만료시킴

        // SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

}
