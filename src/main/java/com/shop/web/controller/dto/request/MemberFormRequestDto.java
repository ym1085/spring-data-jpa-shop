package com.shop.web.controller.dto.request;

import com.shop.domain.member.constant.Role;
import com.shop.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class MemberFormRequestDto {

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Length(min = 2, max = 5, message = "이름은 2글자 또는 5자리까지만 허용이 됩니다.")
    private String userName;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 8, max = 16, message = "비밀번호는 8자리 이상, 16자리 이하로 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[!@#$%^&*+=])(?=\\S+$).{8,16}$",
            message = "비밀번호는 최소 8자리 이상 16자리 이하이어야 하며, 특수문자와 소문자가 모두 포함되어야 합니다.")
    private String password;

    @NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String address;

    @Builder
    public MemberFormRequestDto(String userName, String email, String password, String address) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    public Member toEntity() {
        return Member.builder()
                .userName(userName)
                .email(email)
                .address(address)
                .password(password)
                .role(Role.USER)
                .build();
    }

}
