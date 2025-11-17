package DeliveryNow.Api.infrastructure.adapters.out.repositories.delivery;

import DeliveryNow.Api.domain.entities.enums.DeliveryStatus;
import DeliveryNow.Api.domain.entities.valueObjects.Address;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "Deliveries")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JpaDelivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    @Embedded
    private Address address;
    @NotBlank
    private DeliveryStatus status;
    @NotBlank
    private Long user_id;
    @NotBlank
    private LocalDate expectedDeliveryDate;

    private LocalDateTime deliveredAt;

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
