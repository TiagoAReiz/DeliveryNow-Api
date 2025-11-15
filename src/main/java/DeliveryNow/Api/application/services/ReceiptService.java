package DeliveryNow.Api.application.services;

import DeliveryNow.Api.application.mappers.ReceiptMapper;
import DeliveryNow.Api.application.services.dtos.ReceiptCreateRequest;
import DeliveryNow.Api.application.services.dtos.ReceiptResponse;
import DeliveryNow.Api.application.useCases.ReceiptUseCases;
import DeliveryNow.Api.domain.entities.Receipt;
import DeliveryNow.Api.domain.interfaces.BucketService;
import DeliveryNow.Api.domain.interfaces.ReceiptRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceiptService implements ReceiptUseCases {
    private final ReceiptRepository receiptRepository;
    private final BucketService bucketService;


    public ReceiptService(ReceiptRepository receiptRepository, BucketService bucketService) {
        this.receiptRepository = receiptRepository;
        this.bucketService = bucketService;

    }

    public void create(ReceiptCreateRequest receipt){
        Receipt receiptEntity = new Receipt();
        String imageUrl = bucketService.uploadImage(receipt.image());
        receiptEntity.setImageUrl(imageUrl);
        receiptEntity.setDeliveryId(receipt.deliveryId());
        receiptRepository.createReceipt(receiptEntity);
    }

    @Override
    public List<ReceiptResponse> getAllByDeliveryIdReceipts(Long deliveryId) {

        List<Receipt> receipts = receiptRepository.getAllByDeliveryIdReceipts(deliveryId);
        for(Receipt receipt : receipts){
            String url = bucketService.getBlobUrl(receipt.getImageUrl());
            receipt.setImageUrl(url);

        }
        List<ReceiptResponse> receiptss = receipts.stream().map(ReceiptMapper::toResponse).toList();
        System.out.println(receiptss);
        return receiptss;
    }

    public void deleteReceipt(Long receiptId){
        receiptRepository.deleteReceipt(receiptId);
    }
}
