package io.security.basicsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//@Configuration
//@EnableWebSecurity
public class SecurityConfig { //extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    //@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated();

        http
                .formLogin()
                //.loginPage("/loginPage") .loginPage api를 별다르게 설정하지 않으면, 스프링 시큐리티에서 제공해주는 로그인 페이지가 사용되는데 그때 개발자가 따로 설정한 .usernameParameter, .passwordParameter, .loginProcessingUrl 의 값들이 자동으로 그 로그인 페이지 form 태그에 속성 값으로 설정된다.
                .defaultSuccessUrl("/")
                .failureUrl("/login")
                .usernameParameter("userId")
                .passwordParameter("passwd")
                .loginProcessingUrl("/login_proc") // default는 /login
                .successHandler(new AuthenticationSuccessHandler() { // AuthenticationSuccessHandler interface를 구현한 구현체를 설정하면 됨
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        // Authentication -> 인증에 성공했을 때 그 결과를 담은 인증 객체까지 파리미터로 넘어옴
                        System.out.println("authentication - authentication.getName(): " + authentication.getName()); // authentication - authentication.getName(): user
                        response.sendRedirect("/");
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                        // AuthenticationException -> 인증예외의 객체를 파라미터로 전달해줌
                        System.out.println("AuthenticationException - exception.getMessage(): " + exception.getMessage()); // AuthenticationException - exception.getMessage(): 자격 증명에 실패하였습니다.
                        response.sendRedirect("/login");
                    }
                })
                .permitAll();


        // logout api config
        http
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .addLogoutHandler(new LogoutHandler() {
                    @Override
                    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                        HttpSession session = request.getSession();
                        session.invalidate();
                    }
                })
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        response.sendRedirect("/login");
                    }
                })
                .deleteCookies("remember-me");

        http
                .rememberMe()
                .rememberMeParameter("remember")
                .tokenValiditySeconds(3600) // 1hrs in sec
                .userDetailsService(userDetailsService);

    }

}
