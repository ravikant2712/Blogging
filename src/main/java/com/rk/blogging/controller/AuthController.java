package com.rk.blogging.controller;


import com.rk.blogging.dto.ApiResponseWrapper;
import com.rk.blogging.dto.LoginRequest;
import com.rk.blogging.dto.RegisterRequest;
import com.rk.blogging.model.User;
import com.rk.blogging.services.UserService;
import com.rk.blogging.utils.JWTUtils;
import com.rk.blogging.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User authentication APIs")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JWTUtils jwtUtil;

    @Operation(
            summary = "Register new user",
            description = "Registers a normal USER role account",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User registered successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @PostMapping("/register")
    public  ResponseEntity<ApiResponseWrapper<User>> register(@RequestBody RegisterRequest user) {
        return userService.registerUser(user);
    }

    @Operation(
            summary = "Login user",
            description = "Returns JWT token after successful login"
    )

    @PostMapping("/login")
    public ResponseEntity<ApiResponseWrapper<String>> login(@RequestBody LoginRequest request) {

        System.out.println("Login attempt for user: " + request.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("Login attempt for user: " + request.getUsername());
        String token = jwtUtil.generateToken(request.getUsername());
        return ResponseBuilder.success(
                token,
                "Login successfully",
                HttpStatus.OK
        );
    }
}