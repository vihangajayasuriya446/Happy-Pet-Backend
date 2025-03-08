package com.example.happypet.auth;

import com.example.happypet.config.JwtService;
import com.example.happypet.exception.NotFoundException;
import com.example.happypet.user.Role;
import com.example.happypet.user.UserRepository;
import com.example.happypet.user.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        Optional<Users> isExist = userRepository.findByEmail(request.getEmail());
        if (isExist.isPresent()) {
            throw new DuplicateKeyException("User already exist in the database");
        }
        Users user = new Users();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken, user.getRole().name());
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        Optional<Users> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            Users user = existingUser.get();
            String jwtToken = jwtService.generateToken(user);
            return new AuthenticationResponse(jwtToken, user.getRole().name());
        } else {
            throw new NotFoundException("User does not exist");
        }
    }
}
