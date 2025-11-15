package DeliveryNow.Api.infrastructure.adapters.out.repositories.receipt;

import DeliveryNow.Api.domain.entities.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaReceiptRepository extends JpaRepository<JpaReceipt, Long> {
    List<JpaReceipt> findAllByDeliveryId(Long deliveryId);
}
