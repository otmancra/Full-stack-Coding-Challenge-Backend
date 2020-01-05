package com.coding.challenge.FullstackCodingChallengeBackend.controller;

import com.coding.challenge.FullstackCodingChallengeBackend.exception.ResourceNotFoundException;
import com.coding.challenge.FullstackCodingChallengeBackend.model.User;
import com.coding.challenge.FullstackCodingChallengeBackend.repository.UserRepository;
import com.coding.challenge.FullstackCodingChallengeBackend.security.CurrentUser;
import com.coding.challenge.FullstackCodingChallengeBackend.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }
}
