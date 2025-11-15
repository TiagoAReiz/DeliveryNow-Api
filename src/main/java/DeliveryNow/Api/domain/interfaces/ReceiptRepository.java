package DeliveryNow.Api.domain.interfaces;

import DeliveryNow.Api.domain.entities.Receipt;

import java.util.List;

public interface ReceiptRepository {
    void createReceipt(Receipt receipt);
    List<Receipt> getAllByDeliveryIdReceipts(Long deliveryId);
    void updateReceipt(Long id, Receipt receipt);
    void deleteReceipt(Long id);
}
