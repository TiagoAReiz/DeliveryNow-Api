package DeliveryNow.Api.application.useCases;

import DeliveryNow.Api.application.services.dtos.ReceiptCreateRequest;
import DeliveryNow.Api.application.services.dtos.ReceiptResponse;

import java.util.List;

public interface ReceiptUseCases {
    public void create(ReceiptCreateRequest receipt);
    List<ReceiptResponse> getAllByDeliveryIdReceipts(Long deliveryId);
    void deleteReceipt(Long id);
}
