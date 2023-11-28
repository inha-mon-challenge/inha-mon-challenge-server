package com.example.inhamonchallenge.domain.user.service;

import com.example.inhamonchallenge.domain.user.dto.UserResponse;
import com.example.inhamonchallenge.domain.user.exception.NotFoundUserException;
import com.example.inhamonchallenge.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.inhamonchallenge.global.security.SecurityUtil.getCurrentMemberId;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getUser(Long userId) {
        return UserResponse.from(userRepository.findById(userId).orElseThrow(NotFoundUserException::new));
    }

    public void changePrivacy(boolean isPublic) {
        userRepository.findById(getCurrentMemberId()).ifPresent(user -> user.changePrivacy(isPublic));
    }
}