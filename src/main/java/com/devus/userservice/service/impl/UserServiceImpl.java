package com.devus.userservice.service.impl;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devus.userservice.model.User;
import com.devus.userservice.repository.UserRepository;
import com.devus.userservice.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Transactional
	@Override
	public User saveUser(User user) throws Exception {
		return userRepository.save(user);
	}

	@Transactional
	@Override
	public Page<User> findUser(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	@Override
	public User findUserByEmail(String email) throws NoSuchElementException {
		return userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException());
	}

}
