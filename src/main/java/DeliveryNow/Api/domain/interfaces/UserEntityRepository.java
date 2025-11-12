package DeliveryNow.Api.domain.interfaces;

import DeliveryNow.Api.infrastructure.adapters.out.repositories.userEntity.JpaUserEntity;

public interface UserEntityRepository {
    JpaUserEntity getUserEntityByEmail(String email);
}
