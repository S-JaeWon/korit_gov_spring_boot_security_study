package com.korit.security_study.controller;

import com.korit.security_study.dto.Request.SendMailReqDto;
import com.korit.security_study.dto.Request.VerifyCodeReqDto;
import com.korit.security_study.security.model.Principal;
import com.korit.security_study.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<?> send(
            @RequestBody SendMailReqDto sendMaiReqlDto,
            @AuthenticationPrincipal Principal principal
    ) {
        return ResponseEntity.ok(mailService.sendMail(sendMaiReqlDto, principal));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(
            @RequestBody VerifyCodeReqDto verifyCodeReqDto,
            @AuthenticationPrincipal Principal principal
    ) {
        return ResponseEntity.ok(mailService.verifyCode(verifyCodeReqDto.getVerifyCode(), principal));
    }
}
