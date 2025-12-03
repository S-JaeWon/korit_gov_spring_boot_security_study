package com.korit.security_study.service;

import com.korit.security_study.dto.Request.SendMailReqDto;
import com.korit.security_study.dto.Response.ApiRespDto;
import com.korit.security_study.entity.User;
import com.korit.security_study.entity.UserRole;
import com.korit.security_study.entity.VerifyCode;
import com.korit.security_study.repository.UserRepository;
import com.korit.security_study.repository.UserRoleRepository;
import com.korit.security_study.repository.VerifyCodeRepository;
import com.korit.security_study.security.jwt.JwtUtils;
import com.korit.security_study.security.model.Principal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class MailService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private VerifyCodeRepository verifyCodeRepository;

    @Autowired
    private JavaMailSender javaMailSender;


    public ApiRespDto<?> sendMail(SendMailReqDto sendMailReqDto, Principal principal) {
        if (!sendMailReqDto.getEmail().equals(principal.getEmail())) {
            return new ApiRespDto<>("failed", "잘못된 접근 입니다.", null);
        }

        Optional<User> foundUser = userRepository.getUserByEmail(sendMailReqDto.getEmail());

        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "사용자 정보를 다시 확인해주세요.", null);
        }

        User user = foundUser.get();

        boolean hasTempRole = user.getUserRoles().stream()
                .anyMatch(userRole -> userRole.getRoleId() == 3);

        if (!hasTempRole) {
            return new ApiRespDto<>("failed", "이미 인증된 회원입니다.", null);
        }

        // 랜덤 5자리 코드 생성
        int code = ThreadLocalRandom.current().nextInt(10000, 100000); // 10000 ~ 99999
        String verifyCode = String.valueOf(code);

        verifyCodeRepository.deleteVerifyCode(user.getUserId()); // 기존 코드 삭제

        VerifyCode verify = new VerifyCode();
        verify.setUserId(user.getUserId());
        verify.setVerifyCode(verifyCode);
        verifyCodeRepository.add(verify);

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(sendMailReqDto.getEmail());
        message.setSubject("인증 번호 발송");
        message.setText("해당 인증 번호를 입력해주세요" + "\n" + "인증 번호: " + verify.getVerifyCode());

        javaMailSender.send(message);

        return new ApiRespDto<>("success", "인증 번호가 메일로 발송되었습니다.", null);
    }

    public ApiRespDto<?> verifyCode(String inputCode, Principal principal) {
        Optional<User> foundUser = userRepository.getUserByEmail(principal.getEmail());
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "존재하지 않는 사용자입니다.", null);
        }

        User user = foundUser.get();

        boolean hasTempRole = user.getUserRoles().stream()
                .anyMatch(userRole -> userRole.getRoleId() == 3);

        if (!hasTempRole) {
            return new ApiRespDto<>("failed", "이미 인증된 회원입니다.", null);
        }

        Optional<UserRole> tempUserRole = user.getUserRoles().stream()
                .filter(userRole -> userRole.getRoleId() == 3)
                .findFirst();

        if (tempUserRole.isEmpty()) {
            return new ApiRespDto<>("failed", "이미 인증된 사용자입니다.", null);
        }

        Optional<VerifyCode> foundCode = verifyCodeRepository.findUserByUserId(user.getUserId());
        if (foundCode.isEmpty()) {
            return new ApiRespDto<>("failed", "인증번호를 먼저 요청해주세요.", null);
        }

        VerifyCode verifyCode = foundCode.get();

        if (!verifyCode.getVerifyCode().equals(inputCode)) {
            return new ApiRespDto<>("failed", "인증번호가 일치하지 않습니다.", null);
        }

        UserRole userRole = tempUserRole.get();
        userRole.setRoleId(2);
        userRoleRepository.updateUserRole(userRole);

        verifyCodeRepository.deleteVerifyCode(user.getUserId());

        return new ApiRespDto<>("success", "인증 완료", null);
    }
}
