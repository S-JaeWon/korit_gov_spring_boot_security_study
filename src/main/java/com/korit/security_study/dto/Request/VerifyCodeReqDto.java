package com.korit.security_study.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyCodeReqDto {
    private String verifyCode;
}
