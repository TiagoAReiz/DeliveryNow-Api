package DeliveryNow.Api.application.useCases;

import DeliveryNow.Api.application.services.dtos.*;

import java.util.List;

public interface UserEntityUseCases {
     UserEntityResponse createUser(UserEntityRequest user);
    LoginResponse login(LoginRequest loginRequest);

}
