package DeliveryNow.Api.infrastructure.adapters.out.repositories.receipt;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "receipts")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JpaReceipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotBlank
    String imageUrl;
    @NotBlank
    Long deliveryId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    public void onDelete() {
        this.deletedAt = LocalDateTime.now();
    }
}
