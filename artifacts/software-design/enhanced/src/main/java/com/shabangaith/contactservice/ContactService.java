package com.shabangaith.contactservice;

import java.util.List;

/** Business rules for adding, changing, and deleting contacts. */
public final class ContactService {
    private final ContactRepository repository;

    public ContactService(ContactRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository is required.");
        }
        this.repository = repository;
    }

    public void addContact(Contact contact) {
        if (contact == null) {
            throw new IllegalArgumentException("Contact cannot be null.");
        }
        if (repository.findById(contact.getContactId()).isPresent()) {
            throw new IllegalArgumentException("Contact ID must be unique.");
        }
        repository.save(contact);
    }

    public Contact getContact(String contactId) {
        return getExistingContact(contactId);
    }

    public List<Contact> getAllContacts() {
        return repository.findAll();
    }

    public void deleteContact(String contactId) {
        String validId = ContactValidator.contactId(contactId);
        if (!repository.deleteById(validId)) {
            throw new IllegalArgumentException("Contact ID was not found.");
        }
    }

    public void updateFirstName(String contactId, String firstName) {
        Contact contact = getExistingContact(contactId);
        contact.setFirstName(firstName);
        repository.save(contact);
    }

    public void updateLastName(String contactId, String lastName) {
        Contact contact = getExistingContact(contactId);
        contact.setLastName(lastName);
        repository.save(contact);
    }

    public void updatePhone(String contactId, String phone) {
        Contact contact = getExistingContact(contactId);
        contact.setPhone(phone);
        repository.save(contact);
    }

    public void updateAddress(String contactId, String address) {
        Contact contact = getExistingContact(contactId);
        contact.setAddress(address);
        repository.save(contact);
    }

    private Contact getExistingContact(String contactId) {
        String validId = ContactValidator.contactId(contactId);
        return repository.findById(validId)
                .orElseThrow(() -> new IllegalArgumentException("Contact ID was not found."));
    }
}
