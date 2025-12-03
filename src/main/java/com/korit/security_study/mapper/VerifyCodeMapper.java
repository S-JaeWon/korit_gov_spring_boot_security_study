package com.korit.security_study.mapper;

import com.korit.security_study.entity.VerifyCode;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface VerifyCodeMapper {

    Optional<VerifyCode> findUserByUserId(Integer userId);
    int insertVerifyCode(VerifyCode verifyCode);
    int deleteVerifyCode(Integer userId);
}
