package org.example.board_cafe_kiosk_2603.controller.admin.login;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.config.OtpStore;
import org.example.board_cafe_kiosk_2603.config.SuperKeyProperties;
import org.example.board_cafe_kiosk_2603.mapper.admin.manager.ManagerMapper;
import org.example.board_cafe_kiosk_2603.security.ManagerUserDetailsService;
import org.example.board_cafe_kiosk_2603.security.dto.ManagerDTO;
import org.example.board_cafe_kiosk_2603.service.admin.sms.MailSenderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@Log4j2
@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    /* 관리자 - 2차 인증 컨트롤러 */

    // 역할 분담
    // ManagerLoginSuccessHandler → 1차 인증 후 PRE_AUTH_USER 세션 저장
    // LoginController → 2차 인증 화면 노출 + 검증 + 완전 로그인

    // 방식 분리 이유
    // STAFF  (verifyEmail)    → 폼 방식: 타이머 없는 단순 이메일 입력
    // ADMIN  (verifyEmailOtp) → Ajax 방식: 타이머 유지 필수 + sendOtp도 Ajax

    // 세션 키 규약
    // PRE_AUTH_USER
    // 1차 인증 통과한 loginId (String)
    // → 2차 인증 완료 시 제거

    private final ManagerMapper managerMapper;
    private final MailSenderService mailSenderService;
    private final OtpStore otpStore;
    private final ManagerUserDetailsService managerUserDetailsService;  // 완전 로그인 처리에 사용
    // test 중
    private final SuperKeyProperties superKey;  // 포트폴리오용 슈퍼키

    // ──────────────────────────────────────────────
    // STAFF: 이메일 확인 페이지 GET
    // ──────────────────────────────────────────────
    @GetMapping("/verifyEmail")
    public String verifyEmailPage(HttpSession session, Model model) {
        // PRE_AUTH_USER 없으면 1차 인증 안 한 것 → 로그인 페이지로
        if (session.getAttribute("PRE_AUTH_USER") == null) {
            log.warn("--- [verifyEmail GET] PRE_AUTH_USER 없음 → 로그인 페이지로 리다이렉트 ---");
            return "redirect:/common/login";
        }
        log.info("--- [verifyEmail GET] STAFF 이메일 인증 페이지 진입 ---");
        return "login/verify_email";
    }

    // ──────────────────────────────────────────────
    // STAFF: 이메일 확인 POST (폼 방식)
    // ──────────────────────────────────────────────
    @PostMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("email") String inputEmail,
                              HttpSession session,
                              Model model) {

        String loginId = (String) session.getAttribute("PRE_AUTH_USER");

        // 세션 만료 방어
        if (loginId == null) {
            log.warn("--- [verifyEmail POST] 세션 만료 → 재로그인 유도 ---");
            return "redirect:/common/login?error=session";
        }

        log.info("--- [verifyEmail POST] loginId: {}, 입력 이메일: {} ---", loginId, inputEmail);

        // DB 이메일 조회
        String dbEmail = managerMapper.findEmailByLoginId(loginId).orElse(null);

        if (dbEmail == null || !dbEmail.equals(inputEmail.trim())) {
            log.warn("--- [verifyEmail POST] 이메일 불일치 | DB: {}, 입력: {} ---", dbEmail, inputEmail);
            model.addAttribute("errorMsg", "등록된 이메일 주소와 일치하지 않습니다.");
            return "login/verify_email";
        }

        // 이메일 일치 → 완전 로그인 처리
        log.info("--- [verifyEmail POST] 이메일 일치 → STAFF 완전 로그인 처리 ---");
        completeLogin(loginId, session);

        return "redirect:/admin/dashboard";
    }

    // ──────────────────────────────────────────────
    // ADMIN: OTP 인증 페이지 GET
    // ──────────────────────────────────────────────
    @GetMapping("/verifyEmailOtp")
    public String verifyEmailOtpPage(HttpSession session) {
        if (session.getAttribute("PRE_AUTH_USER") == null) {
            log.warn("--- [verifyEmailOtp GET] PRE_AUTH_USER 없음 → 로그인 페이지로 리다이렉트 ---");
            return "redirect:/common/login";
        }
        log.info("--- [verifyEmailOtp GET] ADMIN OTP 인증 페이지 진입 ---");
        return "login/verify_otp";
    }

    // ──────────────────────────────────────────────
    // ADMIN: OTP 발송 Ajax (POST)
    //   verify_otp.html '인증 요청' 버튼 → fetch('/login/sendOtp')
    // ──────────────────────────────────────────────
    @PostMapping("/sendOtp")
    @ResponseBody
    public ResponseEntity<String> sendOtp(@RequestParam("email") String inputEmail,
                                          HttpSession session) {

        String loginId = (String) session.getAttribute("PRE_AUTH_USER");

        if (loginId == null) {
            log.warn("--- [sendOtp] 세션 만료 ---");
            return ResponseEntity.status(401).body("세션이 만료되었습니다. 다시 로그인해 주세요.");
        }

        log.info("--- [sendOtp] loginId: {}, 입력 이메일: {} ---", loginId, inputEmail);

        // DB 이메일 조회 후 일치 여부 확인
        String dbEmail = managerMapper.findEmailByLoginId(loginId).orElse(null);

        if (dbEmail == null || !dbEmail.equals(inputEmail.trim())) {
            log.warn("--- [sendOtp] 이메일 불일치 | DB: {}, 입력: {} ---", dbEmail, inputEmail);
            return ResponseEntity.status(400).body("등록된 이메일 주소와 일치하지 않습니다.");
        }

        // OTP 생성 → 저장 → 발송
        String code = mailSenderService.generateVerificationCode();
        otpStore.save(dbEmail, code);

        try {
            mailSenderService.sendMailForAlarm(dbEmail, code);
            log.info("--- [sendOtp] OTP 발송 완료 | 이메일: {} ---", dbEmail);
            return ResponseEntity.ok("OTP가 발송되었습니다.");
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("--- [sendOtp] 메일 발송 실패 | 이메일: {}, 원인: {} ---", dbEmail, e.getMessage());
            return ResponseEntity.status(500).body("메일 발송에 실패했습니다. 잠시 후 다시 시도해 주세요.");
        }
    }

    // ──────────────────────────────────────────────
    // ADMIN: OTP 검증 POST
    // ──────────────────────────────────────────────
    //   verify_otp.html 로그인 버튼 → fetch('/login/verifyEmailOtp')
    //   성공 시 { "redirect": "/admin/dashboard" } 응답
    //   실패 시 4xx + 오류 메시지 텍스트 응답
    //   JS가 응답을 받아 화면 갱신 or window.location.href 처리

    @PostMapping("/verifyEmailOtp")
    @ResponseBody  // 폼 방식 → Ajax 방식으로 전환
    public ResponseEntity<String> verifyEmailOtp(@RequestParam("email") String inputEmail,
                                                 @RequestParam("otp") String inputOtp,
                                                 HttpSession session) {

        String loginId = (String) session.getAttribute("PRE_AUTH_USER");

        if (loginId == null) {
            log.warn("--- [verifyEmailOtp POST] 세션 만료 ---");
            return ResponseEntity.status(401).body("세션이 만료되었습니다. 다시 로그인해 주세요.");
        }

        log.info("--- [verifyEmailOtp POST] loginId: {}, 입력 이메일: {}, OTP: {} ---",
                loginId, inputEmail, inputOtp);

        // DB 이메일 검증
        String dbEmail = managerMapper.findEmailByLoginId(loginId).orElse(null);
        if (dbEmail == null || !dbEmail.equals(inputEmail.trim())) {
            log.warn("--- [verifyEmailOtp POST] 이메일 불일치 ---");
            return ResponseEntity.status(400).body("등록된 이메일 주소와 일치하지 않습니다.");
        }

        // OTP 검증 (일치 + 만료 여부 + 1회용 삭제는 OtpStore 내부 처리)
//        if (!otpStore.verify(dbEmail, inputOtp.trim())) {
//            log.warn("--- [verifyEmailOtp POST] OTP 불일치 또는 만료 | 이메일: {} ---", dbEmail);
//            return ResponseEntity.status(400).body("인증번호가 올바르지 않거나 만료되었습니다.");
//        }
        // ⛔️ OTP 검증 - 실제 OTP 또는 슈퍼패스 OTP 중 하나라도 통과하면 인증 성공
        boolean otpValid = otpStore.verify(dbEmail, inputEmail.trim()) || superKey.isSuperOtp(inputOtp.trim());
        if (!otpValid) {
            log.warn("--- [verifyEmailOtp POST] OTP 불일치 또는 만료 | 이메일: {} ---", dbEmail);
            return ResponseEntity.status(400).body("인증번호가 올바르지 않거나 만료되었습니다.");
        }
        // 슈퍼패스 사용 여부 로그
        if (superKey.isSuperOtp(inputOtp.trim())) {
            log.info("--- [verifyEmailOtp POST] 슈퍼패스 OTP 사용 | loginId: {} ---", loginId);
        }
        // ⛔

        // OTP 일치 → 완전 로그인 처리
        log.info("--- [verifyEmailOtp POST] OTP 검증 성공 → ADMIN 완전 로그인 처리 ---");
        completeLogin(loginId, session);

        // JS가 받아서 window.location.href 로 이동할 리다이렉트 경로 반환
        return ResponseEntity.ok("/admin/dashboard");
    }

    // ──────────────────────────────────────────────
    // 공통: 완전 로그인 처리 (SecurityContext 복원)
    // ──────────────────────────────────────────────

    /**
     * 2차 인증 통과 후 Spring Security 인증 상태를 완성시킨다.
     * <p>
     * ManagerLoginSuccessHandler에서 SecurityContext를 의도적으로 제거했기 때문에
     * 여기서 UserDetails를 다시 로드하여 Authentication 객체를 세션에 저장해야
     * 이후 @PreAuthorize, hasRole() 등의 권한 검사가 정상 동작한다.
     *
     * @param loginId 1차 인증 시 PRE_AUTH_USER로 저장해둔 loginId
     * @param session 현재 HTTP 세션
     */
    private void completeLogin(String loginId, HttpSession session) {
        // 1. DB에서 UserDetails 재로드 (ManagerDTO 반환)
        ManagerDTO managerDTO = (ManagerDTO) managerUserDetailsService.loadUserByUsername(loginId);

        // 2. Authentication 객체 생성
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        managerDTO,
                        null,                        // 인증 완료 후이므로 credentials null
                        managerDTO.getAuthorities()
                );

        // 3. SecurityContext에 등록
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        // 4. 세션에 SecurityContext 저장 (이게 없으면 다음 요청에서 인증이 풀림)
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                context
        );

        // 5. 임시 세션 키 제거
        session.removeAttribute("PRE_AUTH_USER");

        log.info("--- [completeLogin] 완전 로그인 완료 | loginId: {}, authorities: {} ---",
                loginId, managerDTO.getAuthorities());
    }
}
