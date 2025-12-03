package com.korit.security_study.controller;

import com.korit.security_study.dto.Request.OAuth2MergeReqDto;
import com.korit.security_study.dto.Request.OAuth2SignupReqDto;
import com.korit.security_study.service.OAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {
    @Autowired
    private OAuth2UserService oAuth2UserService;

    @PostMapping("/signup")
    public ResponseEntity<?> oAuth2Signup(@RequestBody OAuth2SignupReqDto oAuth2SignupReqDto) {
        return ResponseEntity.ok(oAuth2UserService.signup(oAuth2SignupReqDto));
    }

    @PostMapping("/merge")
    public ResponseEntity<?>merge(@RequestBody OAuth2MergeReqDto oAuth2MergeReqDto) {
        return ResponseEntity.ok(oAuth2UserService.merge(oAuth2MergeReqDto));
    }
}
