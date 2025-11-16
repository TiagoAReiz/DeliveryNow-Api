package DeliveryNow.Api.application.mappers;

import DeliveryNow.Api.application.services.dtos.ReceiptResponse;
import DeliveryNow.Api.domain.entities.Receipt;
import DeliveryNow.Api.infrastructure.adapters.out.repositories.receipt.JpaReceipt;
import org.springframework.stereotype.Component;

@Component
public class ReceiptMapper {
    public static ReceiptResponse toResponse(Receipt receipt) {
        return new ReceiptResponse(receipt.getId(),receipt.getImageUrl().replace("http://127.0.0.1", "http://192.168.15.11"));
    }
    public static JpaReceipt toJpa(Receipt receipt) {
        return new JpaReceipt(receipt.getId(), receipt.getImageUrl(),receipt.getDeliveryId());
    }
    public static Receipt toDomain(JpaReceipt receipt) {
        return new Receipt(receipt.getId(),receipt.getImageUrl(),receipt.getDeliveryId());
    }

}
