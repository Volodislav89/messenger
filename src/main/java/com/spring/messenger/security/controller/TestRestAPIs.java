package com.spring.messenger.security.controller;

import com.spring.messenger.security.model.User;
import com.spring.messenger.security.repository.UserRepository;
import com.spring.messenger.security.services.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@AllArgsConstructor
public class TestRestAPIs {
	private UserDetailsServiceImpl userDetailsService;
	private UserRepository userRepository;
	
	@GetMapping("/api/test/user")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public String userAccess(Principal principal) throws Exception {
		UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
		User user = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new Exception("No such User!"));
		return ">>> User Contents! " + principal.getName() + " " + userDetails.getAuthorities() + " " + user.getId();
	}

	@GetMapping("/api/test/pm")
	@PreAuthorize("hasRole('PM') or hasRole('ADMIN')")
	public String projectManagementAccess() {
		return ">>> Board Management Project";
	}
	
	@GetMapping("/api/test/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return ">>> Admin Contents";
	}
}