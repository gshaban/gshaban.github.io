package com.shabangaith.contactservice;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Keeps the contact input rules in one reusable location. Validation happens
 * before data enters the model or is written to storage.
 */
public final class ContactValidator {
    private static final Pattern CONTACT_ID_PATTERN = Pattern.compile("[A-Za-z0-9_-]{1,10}");
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\d{10}");

    private ContactValidator() {
        // Utility class; no instances are needed.
    }

    public static String contactId(String value) {
        String cleaned = requiredText(value, "Contact ID", 10);
        if (!CONTACT_ID_PATTERN.matcher(cleaned).matches()) {
            throw new IllegalArgumentException(
                    "Contact ID must use 1-10 letters, digits, underscores, or hyphens.");
        }
        return cleaned;
    }

    public static String firstName(String value) {
        return requiredText(value, "First name", 10);
    }

    public static String lastName(String value) {
        return requiredText(value, "Last name", 10);
    }

    public static String phone(String value) {
        String cleaned = requiredText(value, "Phone", 10);
        if (!PHONE_PATTERN.matcher(cleaned).matches()) {
            throw new IllegalArgumentException("Phone must be exactly 10 digits.");
        }
        return cleaned;
    }

    public static String address(String value) {
        return requiredText(value, "Address", 30);
    }

    private static String requiredText(String value, String fieldName, int maximumLength) {
        Objects.requireNonNull(value, fieldName + " is required.");
        if (value.chars().anyMatch(Character::isISOControl)) {
            throw new IllegalArgumentException(fieldName + " cannot contain control characters.");
        }
        String cleaned = value.trim();
        if (cleaned.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
        if (cleaned.length() > maximumLength) {
            throw new IllegalArgumentException(fieldName + " cannot exceed " + maximumLength + " characters.");
        }
        return cleaned;
    }
}
