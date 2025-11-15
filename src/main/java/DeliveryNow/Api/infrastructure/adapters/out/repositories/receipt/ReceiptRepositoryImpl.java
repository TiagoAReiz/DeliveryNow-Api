package DeliveryNow.Api.infrastructure.adapters.out.repositories.receipt;

import DeliveryNow.Api.application.mappers.ReceiptMapper;
import DeliveryNow.Api.domain.entities.Receipt;
import DeliveryNow.Api.domain.interfaces.ReceiptRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceiptRepositoryImpl implements ReceiptRepository {

    private final JpaReceiptRepository jpaReceiptRepository;

    public ReceiptRepositoryImpl(JpaReceiptRepository jpaReceiptRepository) {
        this.jpaReceiptRepository = jpaReceiptRepository;
    }

    @Override
    public void createReceipt(Receipt receipt) {
        JpaReceipt jpaReceipt = ReceiptMapper.toJpa(receipt);
        jpaReceiptRepository.save(jpaReceipt);
    }

    @Override
    public List<Receipt> getAllByDeliveryIdReceipts(Long deliveryId) {
        return jpaReceiptRepository.findAllByDeliveryId(deliveryId).stream().map(ReceiptMapper::toDomain).toList();
    }

    @Override
    public void updateReceipt(Long id, Receipt receipt) {
        JpaReceipt jpaReceipt = jpaReceiptRepository.findById(id).orElse(null);
        if (jpaReceipt == null) {
            return;
        }
        receipt.setId(id);
        jpaReceiptRepository.save(ReceiptMapper.toJpa(receipt));
    }

    @Override
    public void deleteReceipt(Long id) {
        JpaReceipt jpaReceipt = jpaReceiptRepository.findById(id).orElse(null);
        if (jpaReceipt == null) {
            return;
        }
        jpaReceiptRepository.delete(jpaReceipt);
    }
}
