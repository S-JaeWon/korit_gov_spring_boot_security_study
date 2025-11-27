package com.korit.security_study.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Integer userId;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;


    private List<UserRole> userRoles;

    /*
    * [User]      [UserRole]
    *      ----->  관리자
    * user ----->  임시 사용자
    *      ----->  사용자
    *
    * 1:N 관계
    * */

}
