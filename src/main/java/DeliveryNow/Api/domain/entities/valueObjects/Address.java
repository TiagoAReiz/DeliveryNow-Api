package DeliveryNow.Api.domain.entities.valueObjects;


public class Address {

    private String city;

    private String street;

    private String zip;

    private String country;

    private String state;

    private String postalCode;

    public Address(String city, String zip, String street, String country, String state, String postalCode) {
        this.city = city;
        this.zip = zip;
        this.street = street;
        this.country = country;
        this.state = state;
        this.postalCode = postalCode;
    }
    public Address() {}

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
