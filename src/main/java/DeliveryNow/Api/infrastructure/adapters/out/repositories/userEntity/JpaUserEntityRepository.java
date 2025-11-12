package DeliveryNow.Api.infrastructure.adapters.out.repositories.userEntity;


import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserEntityRepository extends JpaRepository<JpaUserEntity, Long> {
    JpaUserEntity findByEmail(String email);
}
