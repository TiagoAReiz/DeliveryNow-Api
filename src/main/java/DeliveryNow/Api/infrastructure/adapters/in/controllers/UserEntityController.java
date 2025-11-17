package DeliveryNow.Api.infrastructure.adapters.in.controllers;

import DeliveryNow.Api.application.services.dtos.LoginRequest;
import DeliveryNow.Api.application.services.dtos.LoginResponse;
import DeliveryNow.Api.application.services.dtos.UserEntityRequest;
import DeliveryNow.Api.application.services.dtos.UserEntityResponse;
import DeliveryNow.Api.application.useCases.UserEntityUseCases;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<UserEntityResponse> createUser(@Valid  @RequestBody UserEntityRequest user) {
        try {
            UserEntityResponse userCreated = userEntityUseCases.createUser(user);
            return ResponseEntity.ok(userCreated);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login( @Valid @RequestBody LoginRequest loginRequest) {
        try{
            LoginResponse loginResponse = userEntityUseCases.login(loginRequest);
            return ResponseEntity.ok(loginResponse);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }



    }
}
