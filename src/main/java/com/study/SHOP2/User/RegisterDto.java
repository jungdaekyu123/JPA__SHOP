package com.study.SHOP2.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDto {

    // 아이디: 영어로만 5자 ~ 10자 미만으로 설정
    @NotBlank(message = "아이디를 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z]{5,9}$", message = "아이디는 영어로만 5자 이상, 10자 미만이어야 합니다.")
    private String username;

    // 비밀번호: 5자이상
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^.{5,}$", message = "비밀번호는 5자 이상이어야 합니다.")
    private String password;

    // 닉네임: 2자 이상 30자 이하
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, max = 30, message = "닉네임은 2자 이상 30자 이하여야 합니다.")
    private String displayName;

}
