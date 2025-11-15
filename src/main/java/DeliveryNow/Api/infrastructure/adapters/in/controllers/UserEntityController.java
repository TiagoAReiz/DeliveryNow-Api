package DeliveryNow.Api.infrastructure.adapters.in.controllers;

import DeliveryNow.Api.application.services.dtos.LoginRequest;
import DeliveryNow.Api.application.services.dtos.LoginResponse;
import DeliveryNow.Api.application.services.dtos.UserEntityRequest;
import DeliveryNow.Api.application.services.dtos.UserEntityResponse;
import DeliveryNow.Api.application.useCases.UserEntityUseCases;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class UserEntityController {
    private final UserEntityUseCases userEntityUseCases;

    public UserEntityController(UserEntityUseCases userEntityUseCases) {
        this.userEntityUseCases = userEntityUseCases;
    }
    @PostMapping("/register")
    public ResponseEntity<UserEntityResponse> createUser(@RequestBody UserEntityRequest user) {
        return ResponseEntity.ok(userEntityUseCases.createUser(user));
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userEntityUseCases.login(loginRequest));
    }
}
