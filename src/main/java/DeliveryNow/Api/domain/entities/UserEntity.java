package DeliveryNow.Api.domain.entities;


import java.time.LocalDateTime;

public class UserEntity {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public UserEntity() {
    }

    public UserEntity(Long id, String email, String firstName, String lastName, String password, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }
    public UserEntity( String email, String firstName, String lastName, String password) {

        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.createdAt = null;
        this.updatedAt = null;
        this.deletedAt = null;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
