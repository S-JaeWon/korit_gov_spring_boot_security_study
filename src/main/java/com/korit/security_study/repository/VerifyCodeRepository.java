package com.korit.security_study.repository;

import com.korit.security_study.entity.VerifyCode;
import com.korit.security_study.mapper.VerifyCodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class VerifyCodeRepository {
    @Autowired
    private VerifyCodeMapper verifyCodeMapper;

    public Optional<VerifyCode> findUserByUserId(Integer userId) {
        return verifyCodeMapper.findUserByUserId(userId);
    }

    public int add(VerifyCode verifyCode) {
        return verifyCodeMapper.insertVerifyCode(verifyCode);
    }

    public int deleteVerifyCode(Integer userId) {
        return verifyCodeMapper.deleteVerifyCode(userId);
    }
}
