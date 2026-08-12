"""Unit tests for the enhanced AnimalShelter database repository."""

import unittest
from pathlib import Path
import sys

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))

from animal_shelter import AnimalShelter


class Result:
    def __init__(self, acknowledged=True, modified_count=0, deleted_count=0):
        self.acknowledged = acknowledged
        self.modified_count = modified_count
        self.deleted_count = deleted_count


class FakeCursor(list):
    def sort(self, spec):
        self.sort_spec = spec
        return self

    def limit(self, amount):
        return FakeCursor(self[:amount])


class FakeCollection:
    def __init__(self):
        self.documents = [
            {
                "animal_id": "A100",
                "animal_type": "Dog",
                "breed": "Labrador Retriever Mix",
                "sex_upon_outcome": "Intact Female",
                "age_upon_outcome_in_weeks": 40,
                "outcome_type": "Transfer",
            }
        ]
        self.last_query = None
        self.last_projection = None
        self.last_update = None
        self.created_indexes = []

    def create_index(self, keys, **options):
        self.created_indexes.append((keys, options))
        return options["name"]

    def insert_one(self, data):
        self.documents.append(data)
        return Result(acknowledged=True)

    def find(self, query, projection=None):
        self.last_query = query
        self.last_projection = projection
        return FakeCursor(self.documents)

    def update_many(self, query, update):
        self.last_query = query
        self.last_update = update
        return Result(modified_count=1)

    def delete_many(self, query):
        self.last_query = query
        return Result(deleted_count=1)

    def aggregate(self, pipeline):
        return [{"_id": "Transfer", "count": 1}]


class AnimalShelterRepositoryTests(unittest.TestCase):
    def setUp(self):
        self.collection = FakeCollection()
        self.repo = AnimalShelter(injected_collection=self.collection)

    def test_create_requires_core_animal_fields(self):
        with self.assertRaises(ValueError):
            self.repo.create({"animal_id": "A101"})
        self.assertTrue(
            self.repo.create({"animal_id": "A101", "animal_type": "Dog", "breed": "German Shepherd"})
        )

    def test_read_rejects_unsafe_where_operator(self):
        with self.assertRaises(ValueError):
            self.repo.read({"$where": "this.age > 1"})

    def test_update_wraps_values_in_set_operator(self):
        modified = self.repo.update({"animal_id": "A100"}, {"outcome_type": "Adoption"})
        self.assertEqual(modified, 1)
        self.assertEqual(self.collection.last_update, {"$set": {"outcome_type": "Adoption"}})

    def test_delete_all_requires_explicit_approval(self):
        with self.assertRaises(ValueError):
            self.repo.delete({})
        self.assertEqual(self.repo.delete({}, allow_all=True), 1)

    def test_rescue_profile_query_uses_dashboard_fields(self):
        results = self.repo.find_by_rescue_profile("water", limit=1)
        self.assertEqual(len(results), 1)
        self.assertEqual(self.collection.last_query["animal_type"], "Dog")
        self.assertIn("breed", self.collection.last_query)

    def test_create_indexes_returns_expected_index_names(self):
        names = self.repo.create_indexes()
        self.assertIn("idx_animal_id", names)
        self.assertIn("idx_training_filters", names)

    def test_outcome_summary_uses_aggregation_pipeline(self):
        summary = self.repo.outcome_summary()
        self.assertEqual(summary[0]["_id"], "Transfer")
        self.assertEqual(summary[0]["count"], 1)


if __name__ == "__main__":
    unittest.main()
