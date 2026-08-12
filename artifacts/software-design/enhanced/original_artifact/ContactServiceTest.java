import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ContactServiceTest {
    @Test
    void addsContactWithUniqueId() {
        ContactService service = new ContactService();
        Contact contact = new Contact("C123", "Shaban", "Ghaith", "1234567890", "123 Main Street");

        service.addContact(contact);

        assertEquals(contact, service.getContact("C123"));
    }

    @Test
    void rejectsDuplicateAndNullContacts() {
        ContactService service = new ContactService();
        Contact contact = new Contact("C123", "Shaban", "Ghaith", "1234567890", "123 Main Street");
        service.addContact(contact);

        assertThrows(IllegalArgumentException.class, () -> service.addContact(contact));
        assertThrows(IllegalArgumentException.class, () -> service.addContact(null));
    }

    @Test
    void deletesContactById() {
        ContactService service = new ContactService();
        service.addContact(new Contact("C123", "Shaban", "Ghaith", "1234567890", "123 Main Street"));

        service.deleteContact("C123");

        assertNull(service.getContact("C123"));
    }

    @Test
    void rejectsDeleteForMissingContact() {
        ContactService service = new ContactService();

        assertThrows(IllegalArgumentException.class, () -> service.deleteContact("missing"));
    }

    @Test
    void updatesContactFieldsById() {
        ContactService service = new ContactService();
        service.addContact(new Contact("C123", "Shaban", "Ghaith", "1234567890", "123 Main Street"));

        service.updateFirstName("C123", "John");
        service.updateLastName("C123", "Smith");
        service.updatePhone("C123", "0987654321");
        service.updateAddress("C123", "456 Oak Street");

        Contact updated = service.getContact("C123");
        assertEquals("John", updated.getFirstName());
        assertEquals("Smith", updated.getLastName());
        assertEquals("0987654321", updated.getPhone());
        assertEquals("456 Oak Street", updated.getAddress());
    }

    @Test
    void rejectsUpdateForMissingContact() {
        ContactService service = new ContactService();

        assertThrows(IllegalArgumentException.class, () -> service.updateFirstName("missing", "John"));
    }
}
