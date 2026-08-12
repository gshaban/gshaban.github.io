import java.util.HashMap;
import java.util.Map;

public class ContactService {
    private final Map<String, Contact> contacts = new HashMap<>();

    public void addContact(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null.");
        }
        if (contacts.containsKey(contact.getContactId())) {
            throw new IllegalArgumentException("Contact ID must be unique.");
        }
        contacts.put(contact.getContactId(), contact);
    }

    public void deleteContact(String contactId) {
        if (!contacts.containsKey(contactId)) {
            throw new IllegalArgumentException("Contact ID was not found.");
        }
        contacts.remove(contactId);
    }

    public Contact getContact(String contactId) {
        return contacts.get(contactId);
    }

    public void updateFirstName(String contactId, String firstName) {
        getExistingContact(contactId).setFirstName(firstName);
    }

    public void updateLastName(String contactId, String lastName) {
        getExistingContact(contactId).setLastName(lastName);
    }

    public void updatePhone(String contactId, String phone) {
        getExistingContact(contactId).setPhone(phone);
    }

    public void updateAddress(String contactId, String address) {
        getExistingContact(contactId).setAddress(address);
    }

    private Contact getExistingContact(String contactId) {
        Contact contact = contacts.get(contactId);
        if (contact == null) {
            throw new IllegalArgumentException("Contact ID was not found.");
        }
        return contact;
    }
}
