package com.shabangaith.contactservice;

import java.util.List;
import java.util.Optional;

/** Storage boundary used by the service. */
public interface ContactRepository {
    void save(Contact contact);
    Optional<Contact> findById(String contactId);
    List<Contact> findAll();
    boolean deleteById(String contactId);
}
