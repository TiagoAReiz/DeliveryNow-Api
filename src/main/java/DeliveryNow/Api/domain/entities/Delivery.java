package DeliveryNow.Api.domain.entities;

import DeliveryNow.Api.domain.entities.enums.DeliveryStatus;

public class Delivery {
    private Long id;
    private String name;
    private String address;
    private DeliveryStatus status;


    public Delivery(Long id) {
        this.id = id;
    }


    public Delivery(Long id, String name, String address, DeliveryStatus status) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.status = status;
    }

    public Delivery(String name, String address, DeliveryStatus status) {
        this.name = name;
        this.address = address;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }
}
