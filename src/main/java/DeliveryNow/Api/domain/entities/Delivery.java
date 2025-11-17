package DeliveryNow.Api.domain.entities;

import DeliveryNow.Api.domain.entities.enums.DeliveryStatus;
import DeliveryNow.Api.domain.entities.valueObjects.Address;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Delivery {
    private Long id;
    private String name;
    private Address address;
    private DeliveryStatus status;
    private Long userId;
    private LocalDate expectedDeliveryDate;
    private LocalDateTime deliveredAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public Delivery() {
    }

    public Delivery(String name, Address address, DeliveryStatus status, Long userId, LocalDate expectedDeliveryDate) {
        this.name = name;
        this.address = address;
        this.status = status;
        this.userId = userId;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.deliveredAt = null;
        this.createdAt = null;
        this.updatedAt = null;
        this.deletedAt = null;
    }

    public Delivery(Long id, String name, Address address, DeliveryStatus status, Long userId, LocalDate expectedDeliveryDate, LocalDateTime deliveredAt, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.status = status;
        this.userId = userId;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.deliveredAt = deliveredAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
