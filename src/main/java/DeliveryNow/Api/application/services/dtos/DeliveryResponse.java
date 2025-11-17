package DeliveryNow.Api.application.services.dtos;

import DeliveryNow.Api.domain.entities.enums.DeliveryStatus;
import DeliveryNow.Api.domain.entities.valueObjects.Address;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DeliveryResponse(Long id, String name, Address address, DeliveryStatus status, LocalDate expectedDeliveryDate, LocalDateTime deliveredAt) {
}

