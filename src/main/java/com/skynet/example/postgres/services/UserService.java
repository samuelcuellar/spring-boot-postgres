package com.skynet.example.postgres.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skynet.example.postgres.domain.User;
import com.skynet.example.postgres.dto.UserDto;
import com.skynet.example.postgres.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Transactional( readOnly = true)
	public List<UserDto> getUsers() {
		List<UserDto> result = new ArrayList<UserDto>();
		for (User user: userRepository.findAll()) {
			result.add(new UserDto(user));
		}
		return result;
	}	
	
	@Transactional( readOnly = true)
	public UserDto getUser(Long id) {
		User user = userRepository.findOne(id);
		return new UserDto(user);
	}
	
	@Transactional( readOnly = true)
	public UserDto findByUsername(String username) {
		User user = userRepository.findByUsername(username);
		if (user != null) {
			return new UserDto(user);	
		} else {
			return null;
		}		
	}

	@Transactional
	public void deleteUser(Long id) {
		User user = new User();
		user.setId(id);
		userRepository.delete(user);
	}

	@Transactional
	public void saveUser(UserDto userDto) {
		User user = new User();
		if (userDto.getId() != null) {
			user.setId(userDto.getId());
		}
		user.setUsername(userDto.getUsername());
		userRepository.save(user);
	}
}
