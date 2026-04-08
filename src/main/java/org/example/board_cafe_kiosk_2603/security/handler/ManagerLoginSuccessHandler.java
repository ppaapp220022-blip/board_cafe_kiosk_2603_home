package org.example.board_cafe_kiosk_2603.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Log4j2
@Component  //SecurityConfig에서 생성자 주입을 받기 위해 빈 등록
public class ManagerLoginSuccessHandler implements AuthenticationSuccessHandler {
    // instanceof 분기가 생기는 순간 단일 책임 원칙(SRP) 위반되므로,
    // Kiosk, Admin - LoginSuccessHandler 2EA의 파일로 관리

    // 목적 - ROLE 기반 2차 인증 분기

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // ✅ Role 확인
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // ✅ 세션에 임시 저장 (2차 인증 전까지 loginId 보관)
        HttpSession session = request.getSession();
        session.setAttribute("PRE_AUTH_USER", authentication.getName());

        log.info("--- [AdminLoginSuccess] 1차 인증 성공 | loginId: {}, isAdmin: {} ---",
                authentication.getName(), isAdmin);

        // ✅ SecurityContext 제거 - 2차 인증 전까지 완전 로그인 차단
        SecurityContextHolder.clearContext();
        session.removeAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY
        );

        // ✅ Role에 따라 2차 인증 페이지로 분기
        if (isAdmin) {
            // ADMIN → OTP 인증 페이지
            response.sendRedirect("/login/verifyEmailOtp");
        } else {
            // STAFF → 이메일 확인 페이지
            response.sendRedirect("/login/verifyEmail");
        }

    }
}
