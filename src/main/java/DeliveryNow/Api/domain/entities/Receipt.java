package DeliveryNow.Api.domain.entities;

import java.time.LocalDateTime;

public class Receipt {
    private Long id;
    private String imageUrl;
    private Long deliveryId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public Receipt(Long id, String imageCode, Long deliveryId, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.imageUrl = imageCode;
        this.deliveryId = deliveryId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public Receipt(String imageCode, Long deliveryId) {
        this.imageUrl = imageCode;
        this.deliveryId = deliveryId;
        this.createdAt = null;
        this.updatedAt = null;
        this.deletedAt = null;
    }

    public Receipt() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }
}

