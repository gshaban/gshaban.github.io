# Enhanced Contact Service

**Student:** Shaban Ghaith  
**CS 499 Milestone Two - Enhancement One: Software Design and Engineering**

This project enhances my original CS 320 Contact Service artifact. The original source files are preserved in `original_artifact/`. The enhanced code is in the Maven `src/` folders.

## Enhancements completed

- Moved reusable input rules into `ContactValidator`.
- Added a `ContactRepository` interface so service logic does not depend on one storage implementation.
- Added `FileContactRepository` with durable, atomic local-file persistence.
- Kept validation at the domain boundary and added checks for control characters, empty values, invalid IDs and invalid phone numbers.
- Expanded JUnit coverage for duplicate IDs, missing records, invalid values, persistence after reload and a corrupted storage record.

## Run the tests

1. Install JDK 17 or later and Maven 3.9 or later.
2. Open a terminal in this project folder.
3. Run `mvn test`.

The test suite uses temporary files, so it does not create a permanent contact-data file in the project.

## Design note

The service works with the repository interface rather than with a file directly. This makes the code easier to test with the in-memory repository and leaves room for a different storage option later without changing the service's public behavior.

## Verification

I ran `mvn test` after completing the enhancement. All six JUnit test methods passed.
