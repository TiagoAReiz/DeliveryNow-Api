package DeliveryNow.Api.application.services.dtos;

import DeliveryNow.Api.domain.entities.enums.DeliveryStatus;
import DeliveryNow.Api.domain.entities.valueObjects.Address;

import java.time.LocalDate;

public record DeliveryRequest(String name, Address address, DeliveryStatus status, Long userId, LocalDate expectedDeliveryDate) {
}
