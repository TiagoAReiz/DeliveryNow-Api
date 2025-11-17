package DeliveryNow.Api.infrastructure.adapters.in.controllers;

import DeliveryNow.Api.application.services.dtos.DeliveryRequest;
import DeliveryNow.Api.application.services.dtos.ReceiptCreateRequest;
import DeliveryNow.Api.application.services.dtos.ReceiptResponse;
import DeliveryNow.Api.application.useCases.ReceiptUseCases;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/receipt")
public class ReceiptController {
    private final ReceiptUseCases receiptUseCases;

    public ReceiptController(ReceiptUseCases receiptUseCases) {
        this.receiptUseCases = receiptUseCases;
    }

    @PostMapping("/send/{deliveryId}")
    public ResponseEntity<Void> createReceipt(@RequestParam MultipartFile image, @PathVariable Long deliveryId) {
        ReceiptCreateRequest receipt = new ReceiptCreateRequest(image, deliveryId);
        receiptUseCases.create(receipt);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/delivery/{deliveryId}")
    public ResponseEntity<List<ReceiptResponse>> getReceiptsByDelivery(@PathVariable Long deliveryId) {
        return ResponseEntity.ok(receiptUseCases.getAllByDeliveryIdReceipts(deliveryId));
    }
}
