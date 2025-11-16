package DeliveryNow.Api.infrastructure.adapters.out.repositories.delivery;

import DeliveryNow.Api.application.services.dtos.SearchRequest;
import DeliveryNow.Api.domain.entities.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaDeliveryRepository extends JpaRepository<JpaDelivery, Long> {
    @Query(value = """
SELECT *
FROM deliveries
WHERE user_id = :userId AND
      (:search IS NULL OR name LIKE CONCAT('%', :search, '%') OR address LIKE CONCAT('%', :search, '%'))
  AND (:deliveryStatus IS NULL OR status = :deliveryStatus)
  
""", nativeQuery = true)
    List<JpaDelivery> search(
            @Param("search") String search,
            @Param("deliveryStatus") Integer deliveryStatus,
            @Param("userId") Long userId
    );


}
