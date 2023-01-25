package com.devus.userservice.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.devus.userservice.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	Page<User> findAll(Pageable pageable);
	Optional<User> findByEmail(String email);
}