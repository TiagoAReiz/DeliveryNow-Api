package DeliveryNow.Api.infrastructure.adapters.out.repositories.delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaDeliveryRepository extends JpaRepository<JpaDelivery, Long> {
    @Query(value = """
SELECT *
FROM deliveries
WHERE user_id = :userId
  AND (
        :search IS NULL
        OR LOWER(name) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(city) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(street) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(zip) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(country) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(state) LIKE LOWER(CONCAT('%', :search, '%'))
        OR LOWER(postal_code) LIKE LOWER(CONCAT('%', :search, '%'))
      )
  AND (:deliveryStatus IS NULL OR status = :deliveryStatus)
""", nativeQuery = true)
    List<JpaDelivery> search(
            @Param("search") String search,
            @Param("deliveryStatus") Integer deliveryStatus,
            @Param("userId") Long userId
    );


}
