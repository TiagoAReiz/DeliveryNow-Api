package DeliveryNow.Api.infrastructure.adapters.in.controllers;

import DeliveryNow.Api.application.services.dtos.DeliveryRequest;
import DeliveryNow.Api.application.services.dtos.DeliveryResponse;
import DeliveryNow.Api.application.services.dtos.SearchRequest;
import DeliveryNow.Api.application.useCases.DeliveryUseCases;
import DeliveryNow.Api.domain.entities.enums.DeliveryStatus;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery")
@CrossOrigin(origins = "*")
public class DeliveryController {
    private final DeliveryUseCases deliveryUseCases;

    public DeliveryController(DeliveryUseCases deliveryUseCases) {
        this.deliveryUseCases = deliveryUseCases;
    }
    @GetMapping
    public ResponseEntity<List<DeliveryResponse>> getAllDeliveries() {
        return ResponseEntity.ok(deliveryUseCases.getAllDeliveries());
    }
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryResponse> getDelivery(@Valid @PathVariable Long id) {
        try {
            DeliveryResponse response = deliveryUseCases.getDeliveryById(id);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/search")
    public ResponseEntity<List<DeliveryResponse>> getAllDeliveriesByStatusNameOrAddress(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) DeliveryStatus status,
            @RequestParam(required = true)Long userId ){
        return ResponseEntity.ok(deliveryUseCases.search(new SearchRequest(search, status,userId)));
    }
    @PostMapping
    public ResponseEntity<Void> createDelivery(@Valid @RequestBody DeliveryRequest deliveryRequest) {
        deliveryUseCases.createDelivery(deliveryRequest);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/{id}")
    public ResponseEntity<DeliveryResponse> UpdateDelivery( @Valid @PathVariable Long id, @RequestBody DeliveryRequest deliveryRequest) {
        try {
            DeliveryResponse response = deliveryUseCases.updateDelivery(id, deliveryRequest);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
