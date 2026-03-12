package com.techmarket.authservice.adapter.in.web;

import com.techmarket.authservice.adapter.in.web.dto.LinkTelegramRequest;
import com.techmarket.authservice.adapter.in.web.dto.UpdateUserRequest;
import com.techmarket.authservice.adapter.in.web.dto.UserResponse;
import com.techmarket.authservice.application.port.in.GetUserUseCase;
import com.techmarket.authservice.application.port.in.LinkTelegramUseCase;
import com.techmarket.authservice.application.port.out.UserRepository;
import com.techmarket.authservice.domain.model.User;
import com.techmarket.authservice.infrastructure.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final GetUserUseCase getUserUseCase;
    private final LinkTelegramUseCase linkTelegramUseCase;
    private final UserRepository userRepository;

    public UserController(GetUserUseCase getUserUseCase,
                          LinkTelegramUseCase linkTelegramUseCase,
                          UserRepository userRepository) {
        this.getUserUseCase = getUserUseCase;
        this.linkTelegramUseCase = linkTelegramUseCase;
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            @AuthenticationPrincipal UserPrincipal principal) {
        User user = getUserUseCase.getById(principal.getUserId());
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateCurrentUser(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateUserRequest request) {
        User user = getUserUseCase.getById(principal.getUserId());
        user.setName(request.name());
        User updated = userRepository.save(user);
        return ResponseEntity.ok(UserResponse.from(updated));
    }

    @PostMapping("/me/telegram")
    public ResponseEntity<UserResponse> linkTelegram(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody LinkTelegramRequest request) {
        linkTelegramUseCase.linkTelegram(principal.getUserId(), request.chatId());
        User user = getUserUseCase.getById(principal.getUserId());
        return ResponseEntity.ok(UserResponse.from(user));
    }
}
