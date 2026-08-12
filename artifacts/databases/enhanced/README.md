# CS 499 Enhancement Three: Databases

**Student:** Shaban Ghaith  
**Artifact:** CS 340 Grazioso Salvare MongoDB Dashboard  
**Category:** Databases

## What Was Enhanced

This enhanced artifact improves the database layer used by the original CS 340 dashboard. The original project connected a Dash/Jupyter dashboard to the Austin Animal Center MongoDB collection. The enhancement keeps the same general dashboard purpose, but strengthens the database code so it is more secure, maintainable, reusable, and portfolio-ready.

Major database enhancements include:

- Environment-variable configuration for MongoDB credentials and connection settings.
- URL-safe credential handling using `quote_plus`.
- Connection validation with a MongoDB ping and clear connection errors.
- Centralized query, projection, document, update, and delete validation.
- Protection against unsafe MongoDB operators such as `$where` in user-provided input.
- Safer update logic that applies `$set` internally instead of accepting raw update operators.
- Safer delete logic that blocks accidental full-collection deletion unless `allow_all=True` is explicit.
- Dashboard-specific index creation for animal ID, breed/type filtering, training filters, and outcome summaries.
- Rescue-profile query helpers for water rescue, mountain/wilderness rescue, and disaster/individual tracking.
- Aggregation support for outcome summaries.
- Backward-compatible `CRUD` wrapper so the original dashboard import path still works.
- Unit tests using a fake collection so validation and repository behavior can be verified without needing the original Codio MongoDB server.

## Important Files

- `animal_shelter.py`: Enhanced MongoDB repository and database helper methods.
- `CRUD_Python_Module.py`: Compatibility wrapper for the original dashboard notebook.
- `ProjectTwoDashboard.ipynb`: Original dashboard notebook included with the enhanced database module.
- `aac_shelter_outcomes.csv`: Original data file used by the dashboard package.
- `tests/test_animal_shelter.py`: Unit tests for validation, CRUD behavior, rescue-profile queries, indexes, and aggregation.
- `requirements.txt`: Python package list from the original artifact.

## How to Run the Unit Tests

From the `enhanced_artifact` folder:

```bash
python -m unittest discover -s tests
```

The tests use an injected fake MongoDB collection, so they do not require a live MongoDB database.

## How to Use With MongoDB

Set connection values through environment variables when running outside the original Codio environment:

```bash
set MONGO_USERNAME=aacuser
set MONGO_PASSWORD=YOUR_PASSWORD
set MONGO_HOST=nv-desktop-services.apporto.com
set MONGO_PORT=31580
set MONGO_DATABASE=AAC
set MONGO_COLLECTION=animals
```

Then use the repository from Python:

```python
from animal_shelter import AnimalShelter

repo = AnimalShelter()
repo.create_indexes()
water_candidates = repo.find_by_rescue_profile("water")
summary = repo.outcome_summary()
repo.close()
```

## AI Usage Acknowledgment

I used a generative AI tool to help organize the enhancement package, review rubric alignment, and improve documentation clarity. I reviewed the final code and narrative so the submitted work reflects my selected artifact and intended database enhancement.
