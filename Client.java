package exercise234CMS;

public class Client {
    private int id;
    private String firstName;
    private String lastName;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private String clientType;

    public Client(String firstName, String lastName, String address, String postalCode,
                  String phoneNumber, String clientType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.clientType = clientType;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.clientType = clientType;
    }

    public void updateInfo(String firstName, String lastName, String address, String postalCode,
                           String phoneNumber, String clientType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.clientType = clientType;
        this.address = address;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
        this.clientType = clientType;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getClientType() {
        return clientType;
    }

    public String toString() {
        return this.id + " " + this.firstName + " " + this.lastName + " " + this.clientType;
    }
}
