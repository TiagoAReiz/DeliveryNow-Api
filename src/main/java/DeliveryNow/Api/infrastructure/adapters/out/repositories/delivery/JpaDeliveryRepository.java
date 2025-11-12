package DeliveryNow.Api.infrastructure.adapters.out.repositories.delivery;

import DeliveryNow.Api.application.services.dtos.SearchRequest;
import DeliveryNow.Api.domain.entities.enums.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JpaDeliveryRepository extends JpaRepository<JpaDelivery, Long> {
    @Query("""
    SELECT d FROM JpaDelivery d
    WHERE (:#{#searchRequest.search} IS NULL OR d.name LIKE %:#{#searchRequest.search}% OR d.address LIKE %:#{#searchRequest.search}%)
      AND (:#{#searchRequest.status} IS NULL OR d.status = :#{#searchRequest.status} )
""")
    public List<JpaDelivery> search(SearchRequest searchRequest);

}
