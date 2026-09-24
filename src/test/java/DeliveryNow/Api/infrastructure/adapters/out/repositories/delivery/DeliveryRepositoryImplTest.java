package DeliveryNow.Api.infrastructure.adapters.out.repositories.delivery;

import DeliveryNow.Api.application.services.dtos.DeliveryRequest;
import DeliveryNow.Api.application.services.dtos.SearchRequest;
import DeliveryNow.Api.domain.entities.Delivery;
import DeliveryNow.Api.domain.entities.enums.DeliveryStatus;
import DeliveryNow.Api.domain.entities.valueObjects.Address;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryRepositoryImplTest {

    @Mock
    private JpaDeliveryRepository jpaRepository;

    private DeliveryRepositoryImpl repository;

    private final Address address = new Address("Curitiba", "80000-000", "Rua XV", "BR", "PR", "80000-000");

    @BeforeEach
    void setUp() {
        repository = new DeliveryRepositoryImpl(jpaRepository);
    }

    private JpaDelivery delivery(DeliveryStatus status, LocalDate expected) {
        return new JpaDelivery(1L, "Pedido #1", address, status, 10L, expected, null, null, null, null);
    }

    @Test
    void pendingDeliveryPastExpectedDateIsMarkedLateAndSaved() {
        JpaDelivery overdue = delivery(DeliveryStatus.PENDING, LocalDate.now().minusDays(1));
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(overdue));

        Delivery result = repository.getDeliveryById(1L);

        assertThat(result.getStatus()).isEqualTo(DeliveryStatus.LATE);
        verify(jpaRepository).save(overdue);
    }

    @Test
    void pendingDeliveryDueTodayStaysPending() {
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(delivery(DeliveryStatus.PENDING, LocalDate.now())));

        assertThat(repository.getDeliveryById(1L).getStatus()).isEqualTo(DeliveryStatus.PENDING);
        verify(jpaRepository, never()).save(any());
    }

    @Test
    void deliveredDeliveryIsNeverMarkedLate() {
        when(jpaRepository.findById(1L))
                .thenReturn(Optional.of(delivery(DeliveryStatus.DELIVERED, LocalDate.now().minusDays(5))));

        assertThat(repository.getDeliveryById(1L).getStatus()).isEqualTo(DeliveryStatus.DELIVERED);
        verify(jpaRepository, never()).save(any());
    }

    @Test
    void unknownDeliveryThrowsEntityNotFound() {
        when(jpaRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> repository.getDeliveryById(404L)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void partialUpdateOnlyChangesProvidedFields() {
        JpaDelivery existing = delivery(DeliveryStatus.PENDING, LocalDate.now().plusDays(2));
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(existing));

        Delivery updated = repository.updateDelivery(1L,
                new DeliveryRequest(null, null, DeliveryStatus.DELIVERED, null, null));

        assertThat(updated.getStatus()).isEqualTo(DeliveryStatus.DELIVERED);
        assertThat(updated.getName()).isEqualTo("Pedido #1");
        assertThat(updated.getAddress()).isSameAs(address);
        verify(jpaRepository).save(existing);
    }

    @Test
    void searchPassesStatusOrdinalToNativeQuery() {
        when(jpaRepository.search("curitiba", DeliveryStatus.LATE.ordinal(), 10L))
                .thenReturn(List.of(delivery(DeliveryStatus.LATE, LocalDate.now().minusDays(1))));

        List<Delivery> result = repository.search(new SearchRequest("curitiba", DeliveryStatus.LATE, 10L));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserId()).isEqualTo(10L);
    }

    @Test
    void searchWithoutStatusPassesNull() {
        when(jpaRepository.search(null, null, 10L)).thenReturn(List.of());

        assertThat(repository.search(new SearchRequest(null, null, 10L))).isEmpty();
        verify(jpaRepository).search(null, null, 10L);
    }
}
