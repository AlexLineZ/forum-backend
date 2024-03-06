package com.example.userapp.service.user;

import com.example.userapp.dto.TokenResponse;
import com.example.userapp.dto.request.LoginRequest;
import com.example.userapp.dto.request.RegisterRequest;
import com.example.userapp.dto.response.UserResponse;
import com.example.userapp.entity.User;
import com.example.userapp.jwt.JwtTokenUtils;
import com.example.userapp.mapper.UserMapper;
import com.example.userapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;

    public TokenResponse registerUser(RegisterRequest body) {
        User user = UserMapper.mapRegisterBodyToUser(body);
        userRepository.save(user);

        return TokenResponse.builder()
                .accessToken(jwtTokenUtils.generateToken(user))
                .build();
    }

    public UserResponse getUserResponseByAuthentication(User user) {
        return UserMapper.mapUserToResponse(user);
    }

    public TokenResponse loginUser(LoginRequest body){
        User user = userRepository.findByEmail(body.email())
                .filter(u -> Objects.equals(body.password(), u.getPassword()))
                .orElse(null);

        if (user == null){
            throw new UsernameNotFoundException("Invalid login details");
        }

        return TokenResponse.builder()
                .accessToken(jwtTokenUtils.generateToken(user))
                .build();
    }


    public User getUserByAuthentication(Authentication authentication) {
        UUID id = jwtTokenUtils.getUserIdFromAuthentication(authentication);

        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with ID: " + id + " not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found"));
    }
}
