package DeliveryNow.Api.infrastructure.adapters.out.repositories.delivery;

import DeliveryNow.Api.application.mappers.DeliveryMapper;
import DeliveryNow.Api.application.services.dtos.DeliveryRequest;
import DeliveryNow.Api.application.services.dtos.SearchRequest;
import DeliveryNow.Api.domain.entities.Delivery;
import DeliveryNow.Api.domain.interfaces.DeliveryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryRepositoryImpl implements DeliveryRepository {
    private final JpaDeliveryRepository jpaDeliveryRepository;

    public DeliveryRepositoryImpl(JpaDeliveryRepository jpaDeliveryRepository) {
        this.jpaDeliveryRepository = jpaDeliveryRepository;

    }

    @Override
    public List<Delivery> getAllDeliveries() {
        return jpaDeliveryRepository.findAll().stream().map(DeliveryMapper::toEntity).toList();
    }

    @Override
    public Delivery getDeliveryById(Long id) {
        return DeliveryMapper.toEntity(jpaDeliveryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Delivery not found with id " + id)));
    }

    @Override
    public void createDelivery(Delivery delivery) {
        jpaDeliveryRepository.save(DeliveryMapper.toJpa(delivery));

    }

    @Override
    public List<Delivery> search(SearchRequest searchRequest) {
        return jpaDeliveryRepository.search(searchRequest).stream().map(DeliveryMapper::toEntity).toList();
    }

    @Override
    public Delivery updateDelivery(Long id, DeliveryRequest deliveryRequest) {
        JpaDelivery delivery = jpaDeliveryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Delivery not found with id " + id));
        delivery.setName(deliveryRequest.name());
        delivery.setAddress(deliveryRequest.address());
        delivery.setStatus(deliveryRequest.status());
        jpaDeliveryRepository.save(delivery);
        return DeliveryMapper.toEntity(delivery);
    }
}
