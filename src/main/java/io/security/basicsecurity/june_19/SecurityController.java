package io.security.basicsecurity.june_19;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class SecurityController {

    @GetMapping(value = "/")
    public String index(HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // SecurityContext 객체는 세션에도 저장 됨
        SecurityContext securityContext = (SecurityContext) session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
        Authentication authentication1 = securityContext.getAuthentication();

        return "home";
    }

    @GetMapping(value = "/thread")
    public String thread() {
        /*
        * 메인 스레드하고 자식 스레드 간에 SC객체가 공유가 안 된다. 스레드 로컬이 다르기 때문
        * 그와 관련된 내용을 확인하자*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                /*
                * SC안에는 인증 당시에 인증 필터가 ThreadLocal에 인증 객체를 저장할 때 main ThreadLocal에만 저장하고,
                * 여기 자식 ThreadLocal에는 저장하지 않았기 때문. 따라서 아래 결과는 null값!
                * 하지만, SecurityContextHolder.getContext()을 Alt + F8로 evaluate 해보면, SecurityContext 객체 자체는 존재하나, 그 객체 안의 authentication 맴버 변수는 null 값이 저장 돼 있음*/
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            }
        }).start();

        return "thread";
    }

    @GetMapping(value = "/nologin")
    public String noLogin(HttpServletRequest request) {
        request.changeSessionId(); // java.lang.IllegalStateException: Cannot change session ID. There is no session associated with this request.
        HttpSession httpSession = request.getSession(false);

        if (httpSession == null) {
            System.out.println("null");
        } else {
            System.out.println("not null");
        }

        return "nologin";
    }

    @GetMapping(value = "/nologin2")
    public String noLogin2(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);

        if (httpSession == null) {
            System.out.println("null"); // this line executed...
        } else {
            System.out.println("not null"); // not executed
        }

        return "nologin";
    }

}
