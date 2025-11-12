package DeliveryNow.Api.infrastructure.adapters.out.repositories.userEntity;

import DeliveryNow.Api.domain.interfaces.UserEntityRepository;
import org.springframework.stereotype.Service;

@Service
public class UserEntityRepositoryImpl implements UserEntityRepository {
    private final JpaUserEntityRepository jpaUserEntityRepository;

    public UserEntityRepositoryImpl(JpaUserEntityRepository jpaUserEntityRepository) {
        this.jpaUserEntityRepository = jpaUserEntityRepository;
    }

    @Override
    public JpaUserEntity getUserEntityByEmail(String email) {
        return jpaUserEntityRepository.findByEmail(email);
    }
}
