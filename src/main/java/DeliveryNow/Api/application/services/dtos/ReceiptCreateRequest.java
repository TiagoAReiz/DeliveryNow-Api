package DeliveryNow.Api.application.services.dtos;

import org.springframework.web.multipart.MultipartFile;

public record ReceiptCreateRequest(MultipartFile image, Long deliveryId) {
}
