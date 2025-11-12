package DeliveryNow.Api.application.useCases;

import DeliveryNow.Api.application.services.dtos.DeliveryRequest;
import DeliveryNow.Api.application.services.dtos.DeliveryResponse;
import DeliveryNow.Api.application.services.dtos.SearchRequest;

import java.util.List;

public interface DeliveryUseCases {
    List<DeliveryResponse> getAllDeliveries();
    DeliveryResponse getDeliveryById(Long id);
    void createDelivery(DeliveryRequest deliveryRequest);
    List<DeliveryResponse> search(SearchRequest searchRequest);
    DeliveryResponse updateDelivery(Long id,DeliveryRequest deliveryRequest);

}
