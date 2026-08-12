# CS 499 2-2 Milestone One: Final Recording Guide

Student: Shaban Ghaith

Final submission: MP4 screen recording, about 30 minutes.

Important: Submit the MP4 video, not this guide. This guide is what I should use while recording.

## Recording Setup

Open these three artifact folders before recording:

1. `artifact_source\Category 1 - CS320 Contact Service`
2. `artifact_source\Category 2 - CS300 eBid Binary Search Tree`
3. `artifact_source\Category 3 - CS340 Grazioso MongoDB Dashboard`

Suggested video timing:

- Opening: 2 minutes
- Category One, Software Design and Engineering: 8 minutes
- Category Two, Algorithms and Data Structures: 8 minutes
- Category Three, Databases: 9 minutes
- Closing and AI acknowledgment: 2-3 minutes

## Opening Script

Hi, my name is Shaban Ghaith. For this milestone, I am completing an informal code review of the artifacts I selected for my CS 499 ePortfolio. I am going to review the current state of the code, point out what works, identify weaknesses or limitations using code review criteria, and explain the enhancements I plan to complete during the course. I organized the review into the three required categories: software design and engineering, algorithms and data structures, and databases.

## Category One: Software Design and Engineering

Artifact: CS 320 Contact Service application

Files to show:

- `Contact.java`
- `ContactService.java`
- `ContactTest.java`
- `ContactServiceTest.java`

### Existing Functionality

The Contact Service is a Java application from CS 320. It manages contact records in memory. The `Contact` class stores the contact ID, first name, last name, phone number, and address. The contact ID is final, so it is created once and cannot be changed. The setter methods validate the contact fields before allowing updates.

The `ContactService` class uses a `HashMap<String, Contact>` to store contacts by ID. It supports adding a contact, deleting a contact, updating first name, last name, phone, and address, and retrieving a contact. The test files use JUnit to check valid contacts, invalid input, duplicate IDs, deleting contacts, and update behavior.

### Code Analysis Findings

Positive findings:

- The code separates the contact model from the service class.
- The contact ID is immutable, which protects the unique identifier.
- The service uses a `HashMap`, so lookup by contact ID is efficient.
- Unit tests already exist for normal cases and several invalid cases.

Weaknesses or limitations:

- The application only stores contacts in memory, so data disappears when the program stops.
- Validation is repeated directly inside the model setters. This works, but a separate validation helper would make the rules easier to maintain.
- There is no repository or persistence layer, so the service is tightly tied to the `HashMap`.
- There is no logging or audit trail for create, update, and delete actions.
- The service is not designed for concurrent access. If multiple users or threads used it, the plain `HashMap` could create consistency issues.
- Error handling is simple and uses `IllegalArgumentException`, but a larger application would benefit from clearer custom exceptions.

### Enhancement Plan

My enhancement will refactor the Contact Service into a cleaner layered design. I plan to keep the model, move validation into a reusable validation class, add a repository layer, and add a simple persistence option such as JSON or CSV file storage. I will also expand the tests to cover persistence, invalid updates, duplicate IDs, and edge cases.

Skills demonstrated:

- Object-oriented design
- Separation of concerns
- Validation and secure input handling
- Unit testing
- Maintainable service/repository structure

Course outcome alignment:

This supports professional communication, computing solutions using CS practices, practical tools and techniques, and a security mindset because the enhanced version will validate user data and make the design easier for another developer to maintain.

## Category Two: Algorithms and Data Structures

Artifact: CS 300 eBid Binary Search Tree application

Files to show:

- `BinarySearchTree.cpp`
- `CSVparser.cpp`
- `CSVparser.hpp`
- `eBid_Monthly_Sales.csv`

### Existing Functionality

This artifact is a C++ eBid auction search program from CS 300. It is intended to load bid records from a CSV file, store them in a binary search tree, display all bids, search for a specific bid ID, and remove a bid. The main data structure is a `BinarySearchTree` class that has a root node and methods for insert, remove, search, and traversal.

The CSV parser reads the eBid data file. The `loadBids` function creates `Bid` objects from CSV rows and calls `Insert` to place them in the tree. The menu lets the user load data, display all bids, search by bid ID, remove a bid, or exit.

### Code Analysis Findings

Positive findings:

- The artifact uses a real data structure instead of only storing records in a simple list.
- The program separates bid data, node structure, tree methods, CSV loading, and display logic.
- The menu gives the user a simple way to test loading, searching, traversing, and removing records.
- The CSV parser has error handling for file and row access issues.

Weaknesses or limitations:

- Several binary search tree methods are still placeholder-style or incomplete, including constructor initialization, traversal, insertion, search, and removal.
- The destructor does not currently delete all nodes, so the design risks memory leaks once nodes are created.
- Search returns an empty `Bid` when no match is found, but the current planned logic should be clearer and fully implemented.
- Remove is complex and needs careful handling for leaf nodes, one-child nodes, and two-child nodes.
- Input validation is limited. The CSV loading assumes the expected columns are present and that amount fields can be converted safely.
- A normal binary search tree can become unbalanced depending on insertion order, which can reduce search performance from average O(log n) toward O(n).

### Enhancement Plan

My enhancement will fully implement the binary search tree methods and make the data loading more reliable. I will initialize the root correctly, implement recursive insertion, search, in-order traversal, removal, and safe cleanup in the destructor. I will also add validation for CSV rows and record skipped rows with a clear reason.

I also plan to compare the binary search tree approach to simpler structures like vectors and hash tables. The binary search tree gives sorted traversal and efficient average search, but it has a balance trade-off. I will explain that trade-off in my enhancement narrative and add tests or sample runs that prove insertion, search, sorted traversal, and remove behavior.

Skills demonstrated:

- Binary search tree implementation
- Recursion
- Searching and traversal logic
- Data validation
- Time-complexity trade-off analysis
- Memory management in C++

Course outcome alignment:

This supports the algorithmic outcome directly because I am improving and evaluating a data structure. It also supports communication and tool use because I will explain the performance trade-offs and provide evidence through testing or sample output.

## Category Three: Databases

Artifact: CS 340 Grazioso Salvare Animal Rescue Dashboard / MongoDB application

Files to show:

- `animal_shelter.py`
- `CRUD_Python_Module.py`
- `ProjectTwoDashboard.ipynb`
- `aac_shelter_outcomes.csv`

### Existing Functionality

This artifact is a MongoDB-backed dashboard from CS 340. The database module connects to MongoDB and provides CRUD methods. The dashboard loads shelter animal records, cleans the data into a dataframe, filters records by rescue type, and displays the results through a dashboard table, chart, and map.

The `animal_shelter.py` file contains an `AnimalShelter` class with create, read, update, and delete methods. The dashboard notebook uses a username and password to connect, reads animal records, and includes filtering logic for rescue categories. If the database is not available, the dashboard can fall back to the CSV dataset.

### Code Analysis Findings

Positive findings:

- Database access is separated into a Python module instead of putting all database logic directly in the dashboard.
- CRUD operations exist for create, read, update, and delete.
- The database methods validate that queries and update data are dictionaries before running MongoDB operations.
- The code catches `PyMongoError` and raises clearer runtime errors.
- The dashboard supports user-facing filtering, charting, and mapping, which connects the database to decision-making.

Weaknesses or limitations:

- Credentials appear directly in the dashboard notebook, which is a security weakness even if this was a course environment.
- The connection string is built directly from username and password values. The enhancement should move credentials into environment variables or a configuration file that is not committed.
- The read method returns `list(cursor)`, which can load too much data into memory for large datasets.
- There is no role-based access control in the application layer.
- There is no formal indexing plan for fields that are searched or filtered often.
- The dashboard mixes some data loading, filtering, and display concerns in one notebook, which makes the code harder to test and maintain.

### Enhancement Plan

My enhancement will improve the database layer and dashboard design. I will move credentials out of the notebook and into environment variables, keep database access in a separate module, add safer query construction, and document indexes for fields used in filtering. I will also improve validation and make the dashboard filtering more maintainable by separating filter logic from display logic.

If time allows, I will add role-aware behavior, such as read-only access for normal users and edit access only for approved users. I will also consider pagination or query limits so the dashboard does not load every record at once.

Skills demonstrated:

- MongoDB CRUD operations
- Secure credential handling
- Query validation
- Database indexing
- Dashboard filtering and reporting
- Modular database design

Course outcome alignment:

This supports the database and security parts of the ePortfolio. It also supports professional communication and organizational decision-making because the dashboard turns stored data into useful outputs for rescue-selection decisions.

## Closing Script

To close, these three artifacts give me a strong foundation for the CS 499 enhancements. The Contact Service will let me show software design, testing, validation, and maintainability. The eBid Binary Search Tree will let me show algorithms, data structures, recursion, and performance trade-offs. The Grazioso Salvare dashboard will let me show database design, MongoDB CRUD operations, security, and decision-focused data display.

I used ChatGPT to help organize my code review outline and recording plan. I reviewed the final plan and based the actual code review on my own selected artifacts and code.

## Final Recording Checklist

- Show each artifact folder on screen.
- Show the main files for each category.
- For each category, clearly say: existing functionality, code analysis, enhancement plan.
- Mention at least one security or quality concern in each category.
- Mention course outcomes.
- Keep the tone natural and student-like.
- Submit the exported MP4 file.
