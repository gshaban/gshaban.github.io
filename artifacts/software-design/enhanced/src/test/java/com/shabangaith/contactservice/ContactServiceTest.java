package com.shabangaith.contactservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContactServiceTest {
    private ContactService service;

    @BeforeEach
    void setUp() {
        service = new ContactService(new InMemoryContactRepository());
    }

    @Test
    void addsAndUpdatesAValidContact() {
        service.addContact(new Contact("C123", "Shaban", "Ghaith", "1234567890", "123 Main Street"));
        service.updateFirstName("C123", "John");
        service.updatePhone("C123", "0987654321");

        Contact saved = service.getContact("C123");
        assertEquals("John", saved.getFirstName());
        assertEquals("0987654321", saved.getPhone());
    }

    @Test
    void rejectsDuplicateIdAndMissingRecord() {
        service.addContact(new Contact("C123", "Shaban", "Ghaith", "1234567890", "123 Main Street"));

        assertThrows(IllegalArgumentException.class,
                () -> service.addContact(new Contact("C123", "John", "Smith", "0987654321", "456 Oak Street")));
        assertThrows(IllegalArgumentException.class, () -> service.deleteContact("missing"));
    }

    @Test
    void rejectsInvalidBoundaryInput() {
        assertThrows(IllegalArgumentException.class,
                () -> new Contact("bad id", "Shaban", "Ghaith", "1234567890", "123 Main Street"));
        assertThrows(IllegalArgumentException.class,
                () -> new Contact("C123", " ", "Ghaith", "1234567890", "123 Main Street"));
        assertThrows(IllegalArgumentException.class,
                () -> new Contact("C123", "Shaban", "Ghaith", "12345abcde", "123 Main Street"));
        assertThrows(IllegalArgumentException.class,
                () -> new Contact("C123", "Shaban\n", "Ghaith", "1234567890", "123 Main Street"));
    }
}
