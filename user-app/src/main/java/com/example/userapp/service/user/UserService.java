package com.example.userapp.service.user;

import com.example.common.dto.UserDto;
import com.example.common.exception.UserNotFoundException;
import com.example.security.jwt.JwtTokenUtils;
import com.example.userapp.dto.TokenResponse;
import com.example.userapp.dto.request.LoginRequest;
import com.example.userapp.dto.request.RegisterRequest;
import com.example.userapp.dto.response.UserResponse;
import com.example.userapp.entity.User;
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
                .accessToken(jwtTokenUtils.generateToken(UserMapper.userToUserDto(user)))
                .build();
    }

    public UserResponse getUserResponseByAuthentication(UserDto userDto) {
        User user = getUserByAuthentication(userDto);
        return UserMapper.mapUserToResponse(user);
    }

    public TokenResponse loginUser(LoginRequest body){
        User user = userRepository.findByEmail(body.email())
                .filter(u -> Objects.equals(body.password(), u.getPassword()))
                .orElse(null);

        if (user == null){
            throw new UserNotFoundException("Invalid login details");
        }

        return TokenResponse.builder()
                .accessToken(jwtTokenUtils.generateToken(UserMapper.userToUserDto(user)))
                .build();
    }


    public User getUserByAuthentication(UserDto userDto) {
        return userRepository.findById(userDto.id())
                .orElseThrow(() -> new UsernameNotFoundException("User with ID: " + userDto.id() + " not found"));
    }

    public UserDto getUserByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User with ID: " + userId + " not found"));

        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getRegistrationDate(),
                user.getLastUpdateDate()
        );
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found"));
    }
}
