package DeliveryNow.Api.application.services.dtos;

import DeliveryNow.Api.domain.entities.enums.DeliveryStatus;

public record DeliveryResponse( Long id,String name, String address, DeliveryStatus status ) {
}

