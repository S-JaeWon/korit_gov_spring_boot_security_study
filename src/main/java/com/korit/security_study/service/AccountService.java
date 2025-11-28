package com.korit.security_study.service;

import com.korit.security_study.dto.Request.ModifyPasswordReqDto;
import com.korit.security_study.dto.Response.ApiRespDto;
import com.korit.security_study.entity.User;
import com.korit.security_study.repository.UserRepository;
import com.korit.security_study.security.model.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public ApiRespDto<?> modifyPassword(ModifyPasswordReqDto modifyPasswordReqDto, Principal principal) {
        if (!modifyPasswordReqDto.getUserId().equals(principal.getUserId())) {
            return new ApiRespDto<>("failed", "잘못된 접근입니다.", null);
        }

        Optional<User> foundUser = userRepository.getUserByUserId(modifyPasswordReqDto.getUserId());
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "존재하지 않은 사용자입니다.", null);
        }

        if (!bCryptPasswordEncoder.matches(modifyPasswordReqDto.getOldPassword(), foundUser.get().getPassword())) {
            return new ApiRespDto<>("failed", "기존 비밀번호와 일치하지 않습니다.", null);
        }

        if (bCryptPasswordEncoder.matches(modifyPasswordReqDto.getNewPassword(), foundUser.get().getPassword())) {
            return new ApiRespDto<>("failed", "이미 사용중인 비밀번호 입니다.", null);
        }

        int result = userRepository.updatePassword(modifyPasswordReqDto.toEntity(bCryptPasswordEncoder));

        if (result != 1) {
            return new ApiRespDto<>("failed", "오류 발생", null);
        }

        return new ApiRespDto<>("success", "비밀번호 수정 완료", null);
    }
}
