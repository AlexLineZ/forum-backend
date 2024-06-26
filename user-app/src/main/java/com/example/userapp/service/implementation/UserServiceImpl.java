package com.example.userapp.service.implementation;

import com.example.common.dto.user.UserDto;
import com.example.common.enums.NotificationChannel;
import com.example.common.enums.Role;
import com.example.common.exception.CustomDuplicateFieldException;
import com.example.common.exception.UserNotFoundException;
import com.example.userapp.config.MessageConfig;
import com.example.userapp.dto.TokenResponse;
import com.example.userapp.dto.request.user.LoginRequest;
import com.example.userapp.dto.request.user.RegisterRequest;
import com.example.userapp.dto.request.user.UserUpdateRequest;
import com.example.userapp.dto.response.UserResponse;
import com.example.userapp.entity.ConfirmationToken;
import com.example.userapp.entity.User;
import com.example.userapp.exception.CouldNotVerifyEmailException;
import com.example.userapp.kafka.KafkaProducer;
import com.example.userapp.mapper.UserMapper;
import com.example.userapp.repository.ConfirmationTokenRepository;
import com.example.userapp.repository.UserRepository;
import com.example.userapp.service.TokenService;
import com.example.userapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final KafkaProducer kafkaProducer;

    @Override
    @Transactional
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
        User newUser = userRepository.saveAndFlush(user);

        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setUser(newUser);
        confirmationTokenRepository.save(confirmationToken);

        String emailContent = MessageConfig.EMAIL_MESSAGE
                .replace("{token}", confirmationToken.getConfirmationToken().toString());

        kafkaProducer.sendMessage(
                UserMapper.mapUserToUserNotification(user),
                MessageConfig.SUBJECT_MESSAGE,
                emailContent,
                List.of(NotificationChannel.EMAIL),
                false);

        return tokenService.getTokens(UserMapper.mapUserToUserDto(newUser));
    }

    @Override
    public TokenResponse loginUser(LoginRequest body){
        User user = userRepository.findByEmail(body.email())
                .orElseThrow(() -> new UserNotFoundException("Invalid login details"));

        if (!passwordEncoder.matches(body.password(), user.getPassword())) {
            throw new UserNotFoundException("Invalid login details");
        }

        return tokenService.getTokens(UserMapper.mapUserToUserDto(user));
    }

    @Override
    @Transactional
    public UserResponse updateUser(UserDto currentUser, UserUpdateRequest updateRequest){
        User user = userRepository.findById(currentUser.id())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (updateRequest.getFirstName() != null) user.setFirstName(updateRequest.getFirstName());
        if (updateRequest.getLastName() != null) user.setLastName(updateRequest.getLastName());
        if (updateRequest.getPhone() != null) {
            Optional<User> existingUserWithPhone = userRepository.findByPhone(updateRequest.getPhone());
            if (existingUserWithPhone.isPresent() && !existingUserWithPhone.get().getId().equals(user.getId())) {
                throw new CustomDuplicateFieldException("Phone already exists");
            }
            user.setPhone(updateRequest.getPhone());
        }
        if (updateRequest.getPassword() != null) user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));

        User updatedUser = userRepository.save(user);

        return UserMapper.mapUserToResponse(updatedUser);
    }

    @Override
    public UserResponse getUserByDto(UserDto userDto) {
        User user = getUserByAuthentication(userDto);
        return UserMapper.mapUserToResponse(user);
    }

    @Override
    public User getUserByAuthentication(UserDto userDto) {
        return userRepository.findById(userDto.id())
                .orElseThrow(() -> new UsernameNotFoundException("User with ID: " + userDto.id() + " not found"));
    }

    @Override
    public UserDto getUserByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID: " + userId + " not found"));

        return UserMapper.mapUserToUserDto(user);
    }

    @Override
    @Transactional
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
}
