package com.devus.userservice.controller.dto;

import com.devus.userservice.model.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ReqUserSaveDTO {
	@NotNull
	@Size(max = 30, message = "이메일은 30자 이내로 입력해주세요.")
	String email;
	
	@NotNull
	@Size(max = 20, message = "이름은 20자 이내로 입력해주세요.")
	String name;
	
	@NotNull
	@Size(max = 20, message = "패스워드는 20자 이내로 입력해주세요.")
	String pwd;

	@Size(max = 1000, message = "소개글은 1000자 이내로 입력해주세요.")
	String introduction;
	
	@Size(max = 200, message = "url은 200자 이내로 입력해주세요.")
	String gitUrl;

	@Size(max = 200, message = "url은 200자 이내로 입력해주세요.")
	String webSiteUrl;
	
	@Size(max = 20, message = "그룹명은 20자 이내로 입력해주세요.")
	String groupName;

	@Size(max = 200, message = "url은 200자 이내로 입력해주세요.")
	String pictureUrl;

	public User toModel() {
		return User.builder()
				.email(email)
				.name(name)
				.pwd(pwd)
				.introduction(introduction)
				.gitUrl(gitUrl)
				.webSiteUrl(webSiteUrl)
				.groupName(groupName)
				.pictureUrl(pictureUrl)
				.build();
	}
}