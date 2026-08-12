import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ContactTest {
    @Test
    void createsValidContact() {
        Contact contact = new Contact("C123", "Shaban", "Ghaith", "1234567890", "123 Main Street");

        assertEquals("C123", contact.getContactId());
        assertEquals("Shaban", contact.getFirstName());
        assertEquals("Ghaith", contact.getLastName());
        assertEquals("1234567890", contact.getPhone());
        assertEquals("123 Main Street", contact.getAddress());
    }

    @Test
    void rejectsInvalidContactIds() {
        assertThrows(IllegalArgumentException.class,
                () -> new Contact(null, "Shaban", "Ghaith", "1234567890", "123 Main Street"));
        assertThrows(IllegalArgumentException.class,
                () -> new Contact("12345678901", "Shaban", "Ghaith", "1234567890", "123 Main Street"));
    }

    @Test
    void rejectsInvalidFirstNames() {
        assertThrows(IllegalArgumentException.class,
                () -> new Contact("C123", null, "Ghaith", "1234567890", "123 Main Street"));
        assertThrows(IllegalArgumentException.class,
                () -> new Contact("C123", "TooLongName", "Ghaith", "1234567890", "123 Main Street"));
    }

    @Test
    void rejectsInvalidLastNames() {
        assertThrows(IllegalArgumentException.class,
                () -> new Contact("C123", "Shaban", null, "1234567890", "123 Main Street"));
        assertThrows(IllegalArgumentException.class,
                () -> new Contact("C123", "Shaban", "TooLongName", "1234567890", "123 Main Street"));
    }

    @Test
    void rejectsInvalidPhoneNumbers() {
        assertThrows(IllegalArgumentException.class,
                () -> new Contact("C123", "Shaban", "Ghaith", null, "123 Main Street"));
        assertThrows(IllegalArgumentException.class,
                () -> new Contact("C123", "Shaban", "Ghaith", "123456789", "123 Main Street"));
        assertThrows(IllegalArgumentException.class,
                () -> new Contact("C123", "Shaban", "Ghaith", "123456789A", "123 Main Street"));
    }

    @Test
    void rejectsInvalidAddresses() {
        assertThrows(IllegalArgumentException.class,
                () -> new Contact("C123", "Shaban", "Ghaith", "1234567890", null));
        assertThrows(IllegalArgumentException.class,
                () -> new Contact("C123", "Shaban", "Ghaith", "1234567890", "1234567890123456789012345678901"));
    }
}
