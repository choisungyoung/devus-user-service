package com.devus.userservice.controller.dto;

import com.devus.userservice.model.User;
import lombok.Data;

@Data
public class ResUserFindDTO {
	String email;
	String name;
	String pwd;
	String introduction;
	String gitUrl;
	String webSiteUrl;
	String groupName;
	String pictureUrl;

	public ResUserFindDTO(User dto) {
		this.email = dto.getEmail();
		this.name = dto.getName();
		this.pwd = dto.getPwd();
		this.introduction = dto.getIntroduction();
		this.gitUrl = dto.getGitUrl();
		this.webSiteUrl = dto.getWebSiteUrl();
		this.groupName = dto.getGroupName();
		this.pictureUrl = dto.getPictureUrl();
	}
}