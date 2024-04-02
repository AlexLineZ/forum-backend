package com.example.userapp.service.user;

import com.example.common.dto.UserDto;
import com.example.common.enums.Role;
import com.example.common.exception.CustomDuplicateFieldException;
import com.example.common.exception.UserNotFoundException;
import com.example.security.jwt.JwtTokenUtils;
import com.example.userapp.dto.TokenResponse;
import com.example.userapp.dto.request.LoginRequest;
import com.example.userapp.dto.request.RegisterRequest;
import com.example.userapp.dto.response.UserResponse;
import com.example.userapp.entity.ConfirmationToken;
import com.example.userapp.entity.User;
import com.example.userapp.exception.CouldNotVerifyEmailException;
import com.example.userapp.mapper.UserMapper;
import com.example.userapp.repository.ConfirmationTokenRepository;
import com.example.userapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

import static com.example.userapp.config.MessageConfig.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public TokenResponse registerUser(RegisterRequest body) {
        User user = UserMapper.mapRegisterBodyToUser(body);
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new CustomDuplicateFieldException("Email already exists");
        }
        if (userRepository.existsByPhone(user.getPhone())) {
            throw new CustomDuplicateFieldException("Phone already exists");
        }
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = userRepository.save(user);

        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setUser(newUser);
        confirmationTokenRepository.save(confirmationToken);

        emailService.sendMessageToEmail(user.getEmail(), confirmationToken.getConfirmationToken());

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
                .orElseThrow(() -> new UserNotFoundException("Invalid login details"));

        if (!passwordEncoder.matches(body.password(), user.getPassword())) {
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
                user.getLastUpdateDate(),
                user.isEnabled(),
                user.getRole()
        );
    }

    public void confirmEmail(UUID confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if (token != null)
        {
            User user = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
            user.setEnabled(true);
            userRepository.save(user);
        } else {
            throw new CouldNotVerifyEmailException("Couldn't verify email");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found"));
    }
}
