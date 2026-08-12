package com.shabangaith.contactservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileContactRepositoryTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void reloadsContactsFromPersistentStorage() {
        Path dataFile = temporaryDirectory.resolve("contacts.data");
        ContactService firstRun = new ContactService(new FileContactRepository(dataFile));
        firstRun.addContact(new Contact("C123", "Shaban", "Ghaith", "1234567890", "123 Main Street"));

        ContactService secondRun = new ContactService(new FileContactRepository(dataFile));

        Contact reloaded = secondRun.getContact("C123");
        assertEquals("Shaban", reloaded.getFirstName());
        assertEquals("123 Main Street", reloaded.getAddress());
    }

    @Test
    void persistsUpdatesAndDeletes() {
        Path dataFile = temporaryDirectory.resolve("contacts.data");
        ContactService service = new ContactService(new FileContactRepository(dataFile));
        service.addContact(new Contact("C123", "Shaban", "Ghaith", "1234567890", "123 Main Street"));
        service.updateAddress("C123", "456 Oak Street");
        service.deleteContact("C123");

        ContactService reloaded = new ContactService(new FileContactRepository(dataFile));
        assertEquals(0, reloaded.getAllContacts().size());
    }

    @Test
    void rejectsCorruptedStorageRecord() throws IOException {
        Path dataFile = temporaryDirectory.resolve("contacts.data");
        Files.writeString(dataFile, "not-a-valid-record");

        assertThrows(IllegalStateException.class, () -> new FileContactRepository(dataFile));
    }
}
