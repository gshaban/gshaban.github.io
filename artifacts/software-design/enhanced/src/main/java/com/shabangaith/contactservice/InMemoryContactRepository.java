package com.shabangaith.contactservice;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Small repository implementation used for quick runs and service tests. */
public final class InMemoryContactRepository implements ContactRepository {
    private final Map<String, Contact> contacts = new LinkedHashMap<>();

    @Override
    public void save(Contact contact) { contacts.put(contact.getContactId(), contact); }

    @Override
    public Optional<Contact> findById(String contactId) { return Optional.ofNullable(contacts.get(contactId)); }

    @Override
    public List<Contact> findAll() { return new ArrayList<>(contacts.values()); }

    @Override
    public boolean deleteById(String contactId) { return contacts.remove(contactId) != null; }
}
