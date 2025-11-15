package DeliveryNow.Api.domain.entities;

public class Receipt {
    private Long id;
    private String imageUrl;
    private Long deliveryId;

    public Receipt(Long id, String imageCode, Long deliveryId) {
        this.id = id;
        this.imageUrl = imageCode;
        this.deliveryId = deliveryId;
    }

    public Receipt(String imageCode, Long deliveryId) {
        this.imageUrl = imageCode;
        this.deliveryId = deliveryId;
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
}

