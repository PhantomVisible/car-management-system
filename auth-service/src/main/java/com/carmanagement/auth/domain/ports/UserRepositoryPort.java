package com.carmanagement.auth.domain.ports;

import com.carmanagement.auth.domain.models.User;
import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long userId);
    boolean existsByEmail(String email);
}