package com.devus.userservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.devus.userservice.model.User;

public interface UserService {
	User saveUser(User user) throws Exception;

	Page<User> findUser(Pageable pageable);

	User findUserByEmail(String email);
}
