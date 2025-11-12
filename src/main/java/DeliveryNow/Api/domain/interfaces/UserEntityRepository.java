package DeliveryNow.Api.domain.interfaces;

import DeliveryNow.Api.domain.entities.UserEntity;
import DeliveryNow.Api.infrastructure.adapters.out.repositories.userEntity.JpaUserEntity;

public interface UserEntityRepository {

    JpaUserEntity getUserEntityByEmail(String email);
    UserEntity createUserEntity(UserEntity UserEntity);
}
