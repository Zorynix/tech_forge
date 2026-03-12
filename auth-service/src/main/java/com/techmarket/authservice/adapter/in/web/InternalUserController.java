package com.techmarket.authservice.adapter.in.web;

import com.techmarket.authservice.adapter.in.web.dto.UserResponse;
import com.techmarket.authservice.application.port.in.GetUserUseCase;
import com.techmarket.authservice.application.port.in.LinkTelegramUseCase;
import com.techmarket.authservice.domain.exception.UserNotFoundException;
import com.techmarket.authservice.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/internal/users")
public class InternalUserController {

    private final GetUserUseCase getUserUseCase;
    private final LinkTelegramUseCase linkTelegramUseCase;

    public InternalUserController(GetUserUseCase getUserUseCase,
                                   LinkTelegramUseCase linkTelegramUseCase) {
        this.getUserUseCase = getUserUseCase;
        this.linkTelegramUseCase = linkTelegramUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        try {
            User user = getUserUseCase.getById(id);
            return ResponseEntity.ok(UserResponse.from(user));
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/telegram")
    public ResponseEntity<Void> linkTelegram(@PathVariable UUID id,
                                              @RequestBody Map<String, Long> body) {
        Long chatId = body.get("chatId");
        if (chatId == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            linkTelegramUseCase.linkTelegram(id, chatId);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
