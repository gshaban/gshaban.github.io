public class Contact {
    private final String contactId;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;

    public Contact(String contactId, String firstName, String lastName, String phone, String address) {
        if (!isValidText(contactId, 10)) {
            throw new IllegalArgumentException("Contact ID is required and cannot exceed 10 characters.");
        }
        this.contactId = contactId;
        setFirstName(firstName);
        setLastName(lastName);
        setPhone(phone);
        setAddress(address);
    }

    public String getContactId() {
        return contactId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (!isValidText(firstName, 10)) {
            throw new IllegalArgumentException("First name is required and cannot exceed 10 characters.");
        }
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (!isValidText(lastName, 10)) {
            throw new IllegalArgumentException("Last name is required and cannot exceed 10 characters.");
        }
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (phone == null || !phone.matches("\\d{10}")) {
            throw new IllegalArgumentException("Phone is required and must be exactly 10 digits.");
        }
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (!isValidText(address, 30)) {
            throw new IllegalArgumentException("Address is required and cannot exceed 30 characters.");
        }
        this.address = address;
    }

    private boolean isValidText(String value, int maxLength) {
        return value != null && value.length() <= maxLength;
    }
}
