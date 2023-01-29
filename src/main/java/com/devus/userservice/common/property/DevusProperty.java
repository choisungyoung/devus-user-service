package com.devus.userservice.common.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Component
@ConfigurationProperties("devus")
public class DevusProperty {
	private int maxSourceLength;
}
