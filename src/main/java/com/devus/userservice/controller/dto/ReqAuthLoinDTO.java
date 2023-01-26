package com.devus.userservice.controller.dto;

import com.devus.userservice.model.User;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReqAuthLoinDTO {
	@NotNull
	@Size(max = 30, message = "이메일은 30자 이내로 입력해주세요.")
	String email;

	@NotNull
	@Size(max = 20, message = "패스워드는 20자 이내로 입력해주세요.")
	String pwd;

	public User toModel() {
		return User.builder().email(email).pwd(pwd).build();
	}
}