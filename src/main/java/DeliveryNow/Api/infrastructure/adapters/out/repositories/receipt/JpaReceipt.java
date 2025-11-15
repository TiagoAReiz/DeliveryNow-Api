package DeliveryNow.Api.infrastructure.adapters.out.repositories.receipt;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "receipts")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JpaReceipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String imageUrl;
    Long deliveryId;
}
