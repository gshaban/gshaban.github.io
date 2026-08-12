package com.shabangaith.contactservice;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Persists contacts in a small Base64-delimited data file. Writes use a
 * temporary file followed by a move so a partial write does not replace a
 * previously valid file.
 */
public final class FileContactRepository implements ContactRepository {
    private static final String FIELD_SEPARATOR = "|";
    private final Path storagePath;
    private final Map<String, Contact> contacts = new LinkedHashMap<>();

    public FileContactRepository(Path storagePath) {
        this.storagePath = storagePath.toAbsolutePath().normalize();
        load();
    }

    @Override
    public synchronized void save(Contact contact) {
        contacts.put(contact.getContactId(), contact);
        persist();
    }

    @Override
    public synchronized Optional<Contact> findById(String contactId) {
        return Optional.ofNullable(contacts.get(contactId));
    }

    @Override
    public synchronized List<Contact> findAll() {
        return new ArrayList<>(contacts.values());
    }

    @Override
    public synchronized boolean deleteById(String contactId) {
        boolean removed = contacts.remove(contactId) != null;
        if (removed) {
            persist();
        }
        return removed;
    }

    private void load() {
        if (!Files.exists(storagePath)) {
            return;
        }
        try {
            for (String line : Files.readAllLines(storagePath, StandardCharsets.UTF_8)) {
                if (line.isBlank()) {
                    continue;
                }
                Contact contact = decode(line);
                if (contacts.putIfAbsent(contact.getContactId(), contact) != null) {
                    throw new IllegalStateException("Storage contains a duplicate contact ID.");
                }
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read contact storage.", exception);
        }
    }

    private void persist() {
        Path parent = storagePath.getParent();
        try {
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Path tempFile = Files.createTempFile(parent == null ? Path.of(".") : parent, "contacts-", ".tmp");
            List<String> lines = contacts.values().stream().map(this::encode).toList();
            Files.write(tempFile, lines, StandardCharsets.UTF_8);
            try {
                Files.move(tempFile, storagePath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(tempFile, storagePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Could not save contact storage.", exception);
        }
    }

    private String encode(Contact contact) {
        return String.join(FIELD_SEPARATOR,
                encodeField(contact.getContactId()), encodeField(contact.getFirstName()),
                encodeField(contact.getLastName()), encodeField(contact.getPhone()), encodeField(contact.getAddress()));
    }

    private Contact decode(String line) {
        String[] values = line.split("\\|", -1);
        if (values.length != 5) {
            throw new IllegalStateException("Storage contains an invalid contact record.");
        }
        try {
            return new Contact(decodeField(values[0]), decodeField(values[1]), decodeField(values[2]),
                    decodeField(values[3]), decodeField(values[4]));
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException("Storage contains an invalid contact record.", exception);
        }
    }

    private String encodeField(String value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String decodeField(String value) {
        return new String(Base64.getUrlDecoder().decode(value), StandardCharsets.UTF_8);
    }
}
