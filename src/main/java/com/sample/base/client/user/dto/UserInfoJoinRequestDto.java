package com.sample.base.client.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserInfoJoinRequestDto {
    @NotBlank(message = "사용자 이름을 입력하세요.")
    private String userName;

    @NotBlank(message = "이메일을 입력하세요.")
    private String email;

    @NotBlank
    @Size(min=6,max=20, message = "비밀번호는 최소 6자 이상,20자를 만족해야합니다.")
    private String password;
}
