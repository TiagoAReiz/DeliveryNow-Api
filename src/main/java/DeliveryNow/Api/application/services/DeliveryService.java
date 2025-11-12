package DeliveryNow.Api.application.services;

import DeliveryNow.Api.application.mappers.DeliveryMapper;
import DeliveryNow.Api.application.services.dtos.DeliveryRequest;
import DeliveryNow.Api.application.services.dtos.DeliveryResponse;
import DeliveryNow.Api.application.services.dtos.SearchRequest;
import DeliveryNow.Api.application.useCases.DeliveryUseCases;
import DeliveryNow.Api.domain.interfaces.DeliveryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryService implements DeliveryUseCases {
    private final DeliveryRepository deliveryRepository;
    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }
    @Override
    public List<DeliveryResponse> getAllDeliveries() {
        return deliveryRepository.getAllDeliveries().stream().map(DeliveryMapper::toDto).toList();
    }

    @Override
    public DeliveryResponse getDeliveryById(Long id) {
        return DeliveryMapper.toDto(deliveryRepository.getDeliveryById(id));
    }

    @Override
    public void createDelivery(DeliveryRequest deliveryRequest) {
        deliveryRepository.createDelivery(DeliveryMapper.toEntity(deliveryRequest));
    }

    @Override
    public List<DeliveryResponse> search(SearchRequest searchRequest) {
        return deliveryRepository.search(searchRequest).stream().map(DeliveryMapper::toDto).toList();
    }

    @Override
    public DeliveryResponse updateDelivery(Long id, DeliveryRequest deliveryRequest) {
        return DeliveryMapper.toDto(deliveryRepository.updateDelivery(id, deliveryRequest));
    }
}
