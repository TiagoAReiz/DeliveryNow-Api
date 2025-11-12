package DeliveryNow.Api.domain.interfaces;

import DeliveryNow.Api.application.services.dtos.DeliveryRequest;
import DeliveryNow.Api.application.services.dtos.DeliveryResponse;
import DeliveryNow.Api.application.services.dtos.SearchRequest;
import DeliveryNow.Api.domain.entities.Delivery;

import java.util.List;

public interface DeliveryRepository {
    List<Delivery> getAllDeliveries();
    Delivery getDeliveryById(Long id);
    void createDelivery(Delivery delivery);
    List<Delivery> search(SearchRequest searchRequest);
    Delivery updateDelivery(Long id, DeliveryRequest deliveryRequest);
}
