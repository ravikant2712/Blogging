package com.rk.blogging.services;


import com.rk.blogging.dto.ApiError;
import com.rk.blogging.dto.ApiResponseWrapper;
import com.rk.blogging.dto.RegisterRequest;
import com.rk.blogging.model.User;
import com.rk.blogging.repository.UserRepository;
import com.rk.blogging.utils.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<ApiResponseWrapper<User>> registerUser(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseBuilder.failure(
                    null,
                    "Username already exists",
                    HttpStatus.UNAUTHORIZED
            );
          //  return ResponseEntity.badRequest().body("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseBuilder.failure(
                    null,
                    "Email already exists",
                    HttpStatus.UNAUTHORIZED
            );
        //    return ResponseEntity.badRequest().body("Email already exists");
        }
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : User.Role.USER)
                .build();

        User registerUser  = userRepository.save(user);
        return ResponseBuilder.success(
                registerUser,
                "Register successfully",
                HttpStatus.OK
        );

    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }


    public User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();

        String username;

        if (principal instanceof UserDetails userDetails) {
            username = userDetails.getUsername(); // email or username
        } else {
            username = principal.toString();
        }

        System.out.println("Email.... " + username);

        return userRepository.findByEmail(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found: " + username)
                );
    }
}
