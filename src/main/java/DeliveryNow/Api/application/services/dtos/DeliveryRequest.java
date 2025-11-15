package DeliveryNow.Api.application.services.dtos;

import DeliveryNow.Api.domain.entities.enums.DeliveryStatus;

public record DeliveryRequest(String name, String address, DeliveryStatus status, Long userId) {
}
