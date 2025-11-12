package DeliveryNow.Api.application.mappers;

import DeliveryNow.Api.application.services.dtos.UserEntityRequest;
import DeliveryNow.Api.application.services.dtos.UserEntityResponse;
import DeliveryNow.Api.domain.entities.UserEntity;
import DeliveryNow.Api.infrastructure.adapters.out.repositories.userEntity.JpaUserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {
    public static UserEntity toUserEntity(UserEntityRequest userEntityRequest) {
        return new UserEntity(userEntityRequest.email(), userEntityRequest.firstName(),userEntityRequest.lastName(), userEntityRequest.password());

    }
    public static UserEntityResponse toUserEntityResponse(UserEntity userEntity) {
        return new UserEntityResponse(userEntity.getEmail(),userEntity.getFirstName(), userEntity.getLastName());
    }
    public static JpaUserEntity toJpaUserEntity(UserEntity userEntity) {
        return new JpaUserEntity(userEntity.getId(), userEntity.getEmail(),userEntity.getFirstName(), userEntity.getLastName(), userEntity.getPassword());
    }
}
