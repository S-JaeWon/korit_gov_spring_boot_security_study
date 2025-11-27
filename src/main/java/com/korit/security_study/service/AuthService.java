package com.korit.security_study.service;

import com.korit.security_study.dto.Request.SigninReqDto;
import com.korit.security_study.dto.Request.SignupReqDto;
import com.korit.security_study.dto.Response.ApiRespDto;
import com.korit.security_study.entity.User;
import com.korit.security_study.entity.UserRole;
import com.korit.security_study.repository.UserRepository;
import com.korit.security_study.repository.UserRoleRepository;
import com.korit.security_study.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private JwtUtils jwtUtils;

    public ApiRespDto<?> signup(SignupReqDto signupReqDto) {
        Optional<User> foundUser = userRepository.getUserByUsername(signupReqDto.getUsername());
        if (foundUser.isPresent()) {
            return new ApiRespDto<>("failed", "중복된 username입니다.", null);
        }

        Optional<User> user = userRepository.add(signupReqDto.toEntity(bCryptPasswordEncoder));
        UserRole userRole = UserRole.builder()
                .userId(user.get().getUserId())
                .roleId(3)
                .build();
        userRoleRepository.addUserRole(userRole);

        return new ApiRespDto<>("success", "회원 가입 성공", user.get());
    }

    public ApiRespDto<?> signin(SigninReqDto signinReqDto) {
        Optional<User> foundUser = userRepository.getUserByUsername(signinReqDto.getUsername());
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "아이디 및 비밀번호를 다시 확인해주세요.", null);
        }

        User user = foundUser.get();

        if (!bCryptPasswordEncoder.matches(signinReqDto.getPassword(), user.getPassword())) {
            return new ApiRespDto<>("failed", "아이디 및 비밀번호를 다시 확인해주세요.", null);
        }

        String token = jwtUtils.generateAccessToken(user.getUserId().toString());

        return new ApiRespDto<>("success", "로그인 성공", token);
    }

    public ApiRespDto<?> getUserByUsername(String username) {
        Optional<User> foundUser = userRepository.getUserByUsername(username);
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "조회 할 수 없는 사용자 입니다.", null);
        }

        return new ApiRespDto<>("success", "조회 완료", foundUser.get());
    }

    public ApiRespDto<?> getUserByUserId(Integer userId) {
        Optional<User> foundUser = userRepository.getUserByUserId(userId);
        if (foundUser.isEmpty()) {
            return new ApiRespDto<>("failed", "조회 할 수 없는 사용자 입니다.", null);
        }

        return new ApiRespDto<>("success", "조회 완료", foundUser.get());
    }
}
