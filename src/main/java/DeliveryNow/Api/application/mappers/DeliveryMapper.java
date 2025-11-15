package DeliveryNow.Api.application.mappers;

import DeliveryNow.Api.application.services.dtos.DeliveryRequest;
import DeliveryNow.Api.application.services.dtos.DeliveryResponse;
import DeliveryNow.Api.domain.entities.Delivery;
import DeliveryNow.Api.infrastructure.adapters.out.repositories.delivery.JpaDelivery;
import org.springframework.stereotype.Component;

@Component
public class DeliveryMapper {

    public static DeliveryResponse toDto(Delivery delivery){
        return new DeliveryResponse(delivery.getId(),  delivery.getName(), delivery.getAddress(), delivery.getStatus());

    }
    public static Delivery toEntity(DeliveryRequest delivery){
        return new Delivery(delivery.name(), delivery.address(), delivery.status(), delivery.userId());
    }
    public static JpaDelivery toJpa(Delivery delivery){
        return new JpaDelivery(delivery.getId(), delivery.getName(), delivery.getAddress(), delivery.getStatus(), delivery.getUserId());
    }
    public static Delivery toEntity(JpaDelivery delivery){
        return new Delivery(delivery.getId(), delivery.getName(), delivery.getAddress(), delivery.getStatus(), delivery.getId());
    }
}
