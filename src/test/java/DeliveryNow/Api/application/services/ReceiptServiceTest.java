package DeliveryNow.Api.application.services;

import DeliveryNow.Api.application.services.dtos.ReceiptCreateRequest;
import DeliveryNow.Api.application.services.dtos.ReceiptResponse;
import DeliveryNow.Api.domain.entities.Receipt;
import DeliveryNow.Api.domain.interfaces.BucketService;
import DeliveryNow.Api.domain.interfaces.ReceiptRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReceiptServiceTest {

    @Mock
    private ReceiptRepository receiptRepository;
    @Mock
    private BucketService bucketService;

    private ReceiptService service;

    @BeforeEach
    void setUp() {
        service = new ReceiptService(receiptRepository, bucketService);
    }

    @Test
    void createUploadsImageAndPersistsBlobNameForTheDelivery() {
        var image = new MockMultipartFile("image", "proof.png", "image/png", new byte[]{1, 2, 3});
        when(bucketService.uploadImage(image)).thenReturn("uuid_proof.png");

        service.create(new ReceiptCreateRequest(image, 7L));

        ArgumentCaptor<Receipt> saved = ArgumentCaptor.forClass(Receipt.class);
        verify(receiptRepository).createReceipt(saved.capture());
        assertThat(saved.getValue().getImageUrl()).isEqualTo("uuid_proof.png");
        assertThat(saved.getValue().getDeliveryId()).isEqualTo(7L);
    }

    @Test
    void listingReceiptsResolvesEachBlobToASignedUrl() {
        Receipt first = new Receipt(1L, "a.png", 7L, null, null, null);
        Receipt second = new Receipt(2L, "b.png", 7L, null, null, null);
        when(receiptRepository.getAllByDeliveryIdReceipts(7L)).thenReturn(List.of(first, second));
        when(bucketService.getBlobUrl("a.png")).thenReturn("http://127.0.0.1:10000/devstoreaccount1/c/a.png?sig=1");
        when(bucketService.getBlobUrl("b.png")).thenReturn("http://127.0.0.1:10000/devstoreaccount1/c/b.png?sig=2");

        List<ReceiptResponse> receipts = service.getAllByDeliveryIdReceipts(7L);

        assertThat(receipts).containsExactly(
                new ReceiptResponse(1L, "http://127.0.0.1:10000/devstoreaccount1/c/a.png?sig=1"),
                new ReceiptResponse(2L, "http://127.0.0.1:10000/devstoreaccount1/c/b.png?sig=2"));
    }

    @Test
    void listingReceiptsOfDeliveryWithoutPhotosReturnsEmptyList() {
        when(receiptRepository.getAllByDeliveryIdReceipts(99L)).thenReturn(List.of());

        assertThat(service.getAllByDeliveryIdReceipts(99L)).isEmpty();
        verifyNoInteractions(bucketService);
    }
}
