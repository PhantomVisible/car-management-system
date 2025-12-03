package com.carmanagement.auth.application.services;

import com.carmanagement.auth.domain.models.User;
import com.carmanagement.auth.domain.models.Role;
import com.carmanagement.auth.domain.ports.UserRepositoryPort;
import com.carmanagement.auth.shared.exceptions.InvalidCredentialsException;
import com.carmanagement.auth.shared.exceptions.UserAlreadyExistsException;
import com.carmanagement.auth.shared.exceptions.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.carmanagement.auth.infrastructure.security.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepositoryPort userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
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

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException();
        }
        return user;
    }

    public String authenticateAndGenerateToken(String email, String password) {
        User user = loginUser(email, password); // Reuse existing logic
        return jwtUtil.generateToken(user.getUserId(), user.getEmail(), user.getRole().name());
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}