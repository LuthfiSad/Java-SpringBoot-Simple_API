package com.demo.api.demo_api.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.api.demo_api.helper.ApiResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/dummy-users")
public class DummyUsersController {

	private final Logger logger = org.slf4j.LoggerFactory.getLogger(DummyUsersController.class);
	private final List<User> users = new ArrayList<>();

	public static class User {
		private final UUID id;
		@NotBlank(message = "Name cannot be blank")
		private final String name;
		@Min(value = 1, message = "Age cannot be negative")
		private final int age;

		public User(String name, int age) {
			this.id = UUID.randomUUID();
			this.name = name;
			this.age = age;
		}

		public UUID getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public int getAge() {
			return age;
		}
	}

	public DummyUsersController() {
		users.add(new User("john", 30));
		users.add(new User("jane", 28));
	}

	// @GetMapping
	// public List<User> index() {
	// return users;
	// }

	@GetMapping
	public ResponseEntity<ApiResponse> index() {
		return new ResponseEntity<>(new ApiResponse("Users fetched", users), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<User> create(@Valid @RequestBody User user) {
		logger.info("User created: " + user);
		users.add(user);
		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse> update(@PathVariable String id, @RequestBody User user) {
		Optional<User> userOptional = users.stream().filter(u -> u.getId().toString().equals(id)).findFirst();
		if (!userOptional.isPresent()) {
			return new ResponseEntity<>(new ApiResponse("User not found", null), HttpStatus.NOT_FOUND);
		} else {
			users.remove(userOptional.get());
			users.add(user);
			return new ResponseEntity<>(new ApiResponse("User updated", null), HttpStatus.OK);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> delete(@PathVariable String id) {
		Optional<User> userOptional = users.stream().filter(u -> u.getId().toString().equals(id)).findFirst();
		if (!userOptional.isPresent()) {
			return new ResponseEntity<>(new ApiResponse("User not found", null), HttpStatus.NOT_FOUND);
		} else {
			users.remove(userOptional.get());
			return new ResponseEntity<>(new ApiResponse("User deleted", null), HttpStatus.OK);
		}
	}

}