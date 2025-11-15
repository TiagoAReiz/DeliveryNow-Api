package DeliveryNow.Api.application.services.dtos;

import DeliveryNow.Api.domain.entities.enums.DeliveryStatus;

public record SearchRequest( String search, DeliveryStatus status, Long userId) {
}
