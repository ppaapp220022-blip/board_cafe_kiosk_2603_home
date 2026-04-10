package org.example.board_cafe_kiosk_2603.controller.admin.login;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.board_cafe_kiosk_2603.config.OtpStore;
import org.example.board_cafe_kiosk_2603.config.SuperKeyProperties;
import org.example.board_cafe_kiosk_2603.mapper.admin.manager.ManagerMapper;
import org.example.board_cafe_kiosk_2603.service.admin.manager.ManagerService;
import org.example.board_cafe_kiosk_2603.service.admin.sms.MailSenderService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@Log4j2
@Controller
@RequestMapping("/forgot-password")
@RequiredArgsConstructor
public class ForgotPasswordController {
    /*
     * 비밀번호 찾기 컨트롤러
     *
     * 흐름
     *   STEP 1  GET  /forgot-password           → find_pw.html
     *   STEP 1  POST /forgot-password/verify-id → 아이디 존재 확인
     *   STEP 2  POST /forgot-password/send-otp  → 이메일 대조 + OTP 발송
     *   STEP 2  POST /forgot-password/verify-otp→ OTP 검증 + 임시 비밀번호 발급·발송
     *
     * 세션 키 규약
     *   FORGOT_ID : verify-id 통과한 loginId → verify-otp 완료 시 제거
     */

    private final ManagerMapper managerMapper;
    private final ManagerService managerService;
    private final MailSenderService mailSenderService;
    private final OtpStore otpStore;
    // test 중
    private final SuperKeyProperties superKey;  // 포트폴리오용 슈퍼키

    // ──────────────────────────────────────────────
    // 비밀번호 찾기 페이지 GET
    // ──────────────────────────────────────────────

    @GetMapping
    public String findPwPage() {
        return "login/find_pw";
    }

    // ──────────────────────────────────────────────
    // STEP 1: 아이디 존재 확인 Ajax POST
    // ──────────────────────────────────────────────

    @PostMapping("/verify-id")
    @ResponseBody
    public ResponseEntity<String> verifyId(@RequestParam("loginId") String loginId,
                                           HttpSession session) {

        log.info("--- [forgot/verify-id] 아이디 확인 요청 | loginId: {} ---", loginId);

        boolean exists = managerMapper.findByLoginId(loginId.trim()).isPresent();

        if (!exists) {
            log.warn("--- [forgot/verify-id] 존재하지 않는 아이디: {} ---", loginId);
            return ResponseEntity.status(404).body("존재하지 않는 아이디입니다.");
        }

        // 세션에 저장 — 이후 단계에서 loginId 재사용
        session.setAttribute("FORGOT_ID", loginId.trim());
        log.info("--- [forgot/verify-id] 아이디 확인 성공 | loginId: {} ---", loginId);

        return ResponseEntity.ok("ok");
    }

    // ──────────────────────────────────────────────
    // STEP 2-a: 이메일 대조 + OTP 발송 Ajax POST
    // ──────────────────────────────────────────────

    @PostMapping("/send-otp")
    @ResponseBody
    public ResponseEntity<String> sendOtp(@RequestParam("email") String inputEmail,
                                          HttpSession session) {

        String loginId = (String) session.getAttribute("FORGOT_ID");

        if (loginId == null) {
            log.warn("--- [forgot/send-otp] 세션 만료 ---");
            return ResponseEntity.status(401).body("세션이 만료되었습니다. 처음부터 다시 시도해 주세요.");
        }

        log.info("--- [forgot/send-otp] loginId: {}, 입력 이메일: {} ---", loginId, inputEmail);

        // DB 이메일과 대조
        String dbEmail = managerMapper.findEmailByLoginId(loginId).orElse(null);

        if (dbEmail == null || !dbEmail.equals(inputEmail.trim())) {
            log.warn("--- [forgot/send-otp] 이메일 불일치 | DB: {}, 입력: {} ---", dbEmail, inputEmail);
            return ResponseEntity.status(400).body("등록된 이메일 주소와 일치하지 않습니다.");
        }

        // OTP 생성 → 저장 → 발송
        String code = mailSenderService.generateVerificationCode();
        otpStore.save(dbEmail, code);

        try {
            mailSenderService.sendMailForAlarm(dbEmail, code);
            log.info("--- [forgot/send-otp] OTP 발송 완료 | 이메일: {} ---", dbEmail);
            return ResponseEntity.ok("인증번호가 발송되었습니다.");
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("--- [forgot/send-otp] 메일 발송 실패 | 원인: {} ---", e.getMessage());
            return ResponseEntity.status(500).body("메일 발송에 실패했습니다. 잠시 후 다시 시도해 주세요.");
        }
    }

    // ──────────────────────────────────────────────
    // STEP 2-b: OTP 검증 + 임시 비밀번호 발급·발송 Ajax POST
    // ──────────────────────────────────────────────

    @PostMapping("/verify-otp")
    @ResponseBody
    public ResponseEntity<String> verifyOtp(@RequestParam("email") String inputEmail,
                                            @RequestParam("otp") String inputOtp,
                                            HttpSession session) {

        String loginId = (String) session.getAttribute("FORGOT_ID");

        if (loginId == null) {
            log.warn("--- [forgot/verify-otp] 세션 만료 ---");
            return ResponseEntity.status(401).body("세션이 만료되었습니다. 처음부터 다시 시도해 주세요.");
        }

        log.info("--- [forgot/verify-otp] loginId: {}, 이메일: {}, OTP: {} ---",
                loginId, inputEmail, inputOtp);

        // 이메일 재검증 (세션 탈취 방어)
        String dbEmail = managerMapper.findEmailByLoginId(loginId).orElse(null);
        if (dbEmail == null || !dbEmail.equals(inputEmail.trim())) {
            log.warn("--- [forgot/verify-otp] 이메일 불일치 ---");
            return ResponseEntity.status(400).body("등록된 이메일 주소와 일치하지 않습니다.");
        }

        // OTP 검증 (만료·불일치·1회용 삭제는 OtpStore 내부 처리)
//        if (!otpStore.verify(dbEmail, inputOtp.trim())) {
//            log.warn("--- [forgot/verify-otp] OTP 불일치 또는 만료 ---");
//            return ResponseEntity.status(400).body("인증번호가 올바르지 않거나 만료되었습니다.");
//        }
        // ⛔️ OTP 검증 - 실제 OTP 또는 슈퍼패스 OTP 중 하나라도 통과하면 인증 성공
        boolean usedSuperOtp = superKey.isSuperOtp(inputOtp.trim());
        boolean otpValid = otpStore.verify(dbEmail, inputOtp.trim()) || usedSuperOtp;
        if (!otpValid) {
            log.warn("--- [forgot/verify-otp] OTP 불일치 또는 만료 ---");
            return ResponseEntity.status(400).body("인증번호가 올바르지 않거나 만료되었습니다.");
        }

        // ✅ 슈퍼패스 사용 여부 로그 (시연 추적용)
        if (usedSuperOtp) {
            // 슈퍼패스: 고정 임시 비밀번호 DB 저장 + 메일 발송 생략
            managerService.resetPasswordTo(loginId, superKey.getTempPasswd());
            log.info("--- [forgot/verify-otp] 슈퍼패스 임시 비밀번호 적용 | loginId: {}, tempPasswd: {} ---",
                    loginId, superKey.getTempPasswd());
        } else {
            // 일반: 랜덤 임시 비밀번호 생성 + 메일 발송
            String tempPassword = managerService.resetPassword(loginId);
            try {
                mailSenderService.sendTempPassword(dbEmail, tempPassword);
                log.info("--- [forgot/verify-otp] 임시 비밀번호 발송 완료 | loginId: {} ---", loginId);
            } catch (MessagingException | UnsupportedEncodingException e) {
                log.error("--- [forgot/verify-otp] 임시 비밀번호 메일 발송 실패 | 원인: {} ---", e.getMessage());
                return ResponseEntity.status(500).body("임시 비밀번호 발송에 실패했습니다. 잠시 후 다시 시도해 주세요.");
            }
        }

        // ⛔️


        // 임시 비밀번호 생성 → DB 저장 → 이메일 발송
//        String tempPassword = managerService.resetPassword(loginId);
//
//        try {
//            mailSenderService.sendTempPassword(dbEmail, tempPassword);
//            log.info("--- [forgot/verify-otp] 임시 비밀번호 발송 완료 | loginId: {} ---", loginId);
//        } catch (MessagingException | UnsupportedEncodingException e) {
//            log.error("--- [forgot/verify-otp] 임시 비밀번호 메일 발송 실패 | 원인: {} ---", e.getMessage());
//            return ResponseEntity.status(500).body("임시 비밀번호 발송에 실패했습니다. 잠시 후 다시 시도해 주세요.");
//        }

        // 세션 정리
        session.removeAttribute("FORGOT_ID");
        log.info("--- [forgot/verify-otp] 비밀번호 재설정 완료 | loginId: {} ---", loginId);

        return ResponseEntity.ok("ok");
    }
}
