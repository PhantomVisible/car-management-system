package com.carmanagement.auth.application.services;

import com.carmanagement.auth.domain.models.User;
import com.carmanagement.auth.domain.models.Role;
import com.carmanagement.auth.domain.ports.UserRepositoryPort;
import com.carmanagement.auth.shared.exceptions.UserAlreadyExistsException;
import com.carmanagement.auth.shared.exceptions.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepositoryPort userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String email, String password, Role role) {
        // Checks if user already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException(email);
        }

        // Create new user
        User user = new User(email, passwordEncoder.encode(password), role);
        return userRepository.save(user);
    }

    public User loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        // We'll add password verification when we implement JWT
        return user;
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}