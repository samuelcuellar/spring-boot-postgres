package com.skynet.example.postgres.repository;

import org.springframework.data.repository.CrudRepository;

import com.skynet.example.postgres.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	User findByUsername(String username);

}
