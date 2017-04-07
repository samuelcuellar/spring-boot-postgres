package com.skynet.example.postgres.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.skynet.example.postgres.dto.UserDto;
import com.skynet.example.postgres.services.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService; 

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<UserDto> getUsers() throws Exception {
		return userService.getUsers();
	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public UserDto getUser(@PathVariable Long id) throws Exception {
		return userService.getUser(id);
	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
	public void deleteUser(@PathVariable Long id) throws Exception {
		userService.deleteUser(id);
	}

	@RequestMapping(value = "/users", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void saveUser(@RequestBody UserDto userDto) throws Exception {
		userService.saveUser(userDto);
	}

}
