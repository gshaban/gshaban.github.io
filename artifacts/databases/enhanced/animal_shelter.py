"""Enhanced MongoDB data access layer for the Grazioso Salvare dashboard.

Student: Shaban Ghaith
Course: CS 499 Computer Science Capstone
Enhancement category: Databases

This module keeps the original CS 340 dashboard API while improving database
configuration, validation, query helpers, indexing, error handling, and security.
"""

from __future__ import annotations

import os
from typing import Any, Dict, Iterable, List, Mapping, Optional, Sequence, Tuple
from urllib.parse import quote_plus

try:
    from pymongo import ASCENDING, MongoClient
    from pymongo.collection import Collection
    from pymongo.errors import PyMongoError
except ImportError:  # Allows tests/static review without a local MongoDB install.
    ASCENDING = 1
    MongoClient = None
    Collection = Any

    class PyMongoError(Exception):
        """Fallback error type used only when pymongo is unavailable."""


Document = Dict[str, Any]
Query = Dict[str, Any]
Projection = Dict[str, int]
SortSpec = Sequence[Tuple[str, int]]


class AnimalShelter:
    """Repository for the AAC animals collection in MongoDB.

    The class centralizes all database access for the dashboard so queries,
    validation, indexes, and error handling are maintained in one place.
    """

    PROFILE_QUERIES: Mapping[str, Query] = {
        "water": {
            "animal_type": "Dog",
            "breed": {
                "$in": [
                    "Labrador Retriever Mix",
                    "Chesapeake Bay Retriever",
                    "Newfoundland",
                ]
            },
            "sex_upon_outcome": "Intact Female",
            "age_upon_outcome_in_weeks": {"$gte": 26, "$lte": 156},
        },
        "mountain": {
            "animal_type": "Dog",
            "breed": {
                "$in": [
                    "German Shepherd",
                    "Alaskan Malamute",
                    "Old English Sheepdog",
                    "Siberian Husky",
                    "Rottweiler",
                ]
            },
            "sex_upon_outcome": "Intact Male",
            "age_upon_outcome_in_weeks": {"$gte": 26, "$lte": 156},
        },
        "disaster": {
            "animal_type": "Dog",
            "breed": {
                "$in": [
                    "Doberman Pinscher",
                    "German Shepherd",
                    "Golden Retriever",
                    "Bloodhound",
                    "Rottweiler",
                ]
            },
            "sex_upon_outcome": "Intact Male",
            "age_upon_outcome_in_weeks": {"$gte": 20, "$lte": 300},
        },
    }

    DEFAULT_PROJECTION: Projection = {
        "_id": 0,
        "animal_id": 1,
        "animal_type": 1,
        "breed": 1,
        "sex_upon_outcome": 1,
        "age_upon_outcome_in_weeks": 1,
        "outcome_type": 1,
        "name": 1,
        "location_lat": 1,
        "location_long": 1,
    }

    def __init__(
        self,
        username: Optional[str] = None,
        password: Optional[str] = None,
        host: Optional[str] = None,
        port: Optional[int] = None,
        database: Optional[str] = None,
        collection: Optional[str] = None,
        auth_source: Optional[str] = None,
        timeout_ms: int = 5000,
        injected_collection: Optional[Collection] = None,
    ) -> None:
        self.username = username or os.getenv("MONGO_USERNAME", "aacuser")
        self.password = password or os.getenv("MONGO_PASSWORD")
        self.host = host or os.getenv("MONGO_HOST", "nv-desktop-services.apporto.com")
        self.port = int(port or os.getenv("MONGO_PORT", "31580"))
        self.database_name = database or os.getenv("MONGO_DATABASE", "AAC")
        self.collection_name = collection or os.getenv("MONGO_COLLECTION", "animals")
        self.auth_source = auth_source or os.getenv("MONGO_AUTH_SOURCE", "admin")
        self.timeout_ms = timeout_ms
        self.client = None
        self.database = None

        if injected_collection is not None:
            self.collection = injected_collection
            return

        if MongoClient is None:
            raise ImportError("pymongo is required to connect to MongoDB. Install dependencies from requirements.txt.")

        username = quote_plus(self.username)
        password = quote_plus(self.password)
        uri = (
            f"mongodb://{username}:{password}@{self.host}:{self.port}/"
            f"{self.database_name}?authSource={quote_plus(self.auth_source)}"
        )
        try:
            self.client = MongoClient(uri, serverSelectionTimeoutMS=self.timeout_ms)
            self.client.admin.command("ping")
            self.database = self.client[self.database_name]
            self.collection = self.database[self.collection_name]
        except PyMongoError as err:
            raise ConnectionError(f"Could not connect to MongoDB collection {self.database_name}.{self.collection_name}.") from err

    def create_indexes(self) -> List[str]:
        """Create dashboard-focused indexes and return their names."""
        index_specs = [
            ([('animal_id', ASCENDING)], {"name": "idx_animal_id", "unique": True}),
            ([('animal_type', ASCENDING), ('breed', ASCENDING)], {"name": "idx_type_breed"}),
            ([('sex_upon_outcome', ASCENDING), ('age_upon_outcome_in_weeks', ASCENDING)], {"name": "idx_training_filters"}),
            ([('outcome_type', ASCENDING)], {"name": "idx_outcome_type"}),
        ]
        try:
            return [self.collection.create_index(keys, **options) for keys, options in index_specs]
        except PyMongoError as err:
            raise RuntimeError("Index creation failed.") from err

    def create(self, data: Document) -> bool:
        """Insert one valid animal document and return True when acknowledged."""
        self._validate_document(data)
        try:
            result = self.collection.insert_one(data)
            return bool(result.acknowledged)
        except PyMongoError as err:
            raise RuntimeError("Create operation failed.") from err

    def read(
        self,
        query: Optional[Query] = None,
        projection: Optional[Projection] = None,
        limit: int = 0,
        sort: Optional[SortSpec] = None,
    ) -> List[Document]:
        """Return documents that match a validated MongoDB query."""
        query = self._validate_query(query or {})
        projection = self._validate_projection(projection) if projection is not None else projection
        try:
            cursor = self.collection.find(query, projection)
            if sort:
                cursor = cursor.sort(list(sort))
            if limit:
                cursor = cursor.limit(limit)
            return list(cursor)
        except PyMongoError as err:
            raise RuntimeError("Read operation failed.") from err

    def read_dataframe(self, query: Optional[Query] = None, projection: Optional[Projection] = None):
        """Return query results as a pandas DataFrame for dashboard rendering."""
        try:
            import pandas as pd
        except ImportError as err:
            raise ImportError("pandas is required for read_dataframe().") from err
        records = self.read(query=query, projection=projection)
        return pd.DataFrame.from_records(records)

    def update(self, query: Query, update_values: Document) -> int:
        """Update matching documents using a safe $set operation."""
        query = self._validate_query(query)
        update_values = self._validate_update_values(update_values)
        try:
            result = self.collection.update_many(query, {"$set": update_values})
            return int(result.modified_count)
        except PyMongoError as err:
            raise RuntimeError("Update operation failed.") from err

    def delete(self, query: Query, allow_all: bool = False) -> int:
        """Delete matching documents; empty deletes require explicit approval."""
        query = self._validate_query(query)
        if not query and not allow_all:
            raise ValueError("Refusing to delete every document without allow_all=True.")
        try:
            result = self.collection.delete_many(query)
            return int(result.deleted_count)
        except PyMongoError as err:
            raise RuntimeError("Delete operation failed.") from err

    def rescue_profile_query(self, profile: str) -> Query:
        """Return a copy of the query used for a rescue training profile."""
        key = profile.strip().lower()
        if key not in self.PROFILE_QUERIES:
            valid = ", ".join(sorted(self.PROFILE_QUERIES))
            raise ValueError(f"Unknown rescue profile '{profile}'. Valid profiles: {valid}.")
        return dict(self.PROFILE_QUERIES[key])

    def find_by_rescue_profile(self, profile: str, limit: int = 0) -> List[Document]:
        """Find animals that match a Grazioso Salvare rescue profile."""
        return self.read(
            query=self.rescue_profile_query(profile),
            projection=self.DEFAULT_PROJECTION,
            limit=limit,
            sort=[("breed", ASCENDING), ("animal_id", ASCENDING)],
        )

    def outcome_summary(self) -> List[Document]:
        """Aggregate records by outcome type for dashboard summary cards/charts."""
        pipeline = [
            {"$group": {"_id": "$outcome_type", "count": {"$sum": 1}}},
            {"$sort": {"count": -1, "_id": 1}},
        ]
        try:
            return list(self.collection.aggregate(pipeline))
        except PyMongoError as err:
            raise RuntimeError("Outcome summary aggregation failed.") from err

    def close(self) -> None:
        """Close the MongoDB client when the dashboard or script is finished."""
        if self.client is not None:
            self.client.close()

    @staticmethod
    def _validate_document(data: Document) -> None:
        if not isinstance(data, dict) or not data:
            raise ValueError("Data must be a non-empty dictionary.")
        required = {"animal_id", "animal_type", "breed"}
        missing = sorted(required.difference(data))
        if missing:
            raise ValueError(f"Animal document is missing required field(s): {', '.join(missing)}.")
        AnimalShelter._reject_unsafe_keys(data)

    @staticmethod
    def _validate_query(query: Query) -> Query:
        if not isinstance(query, dict):
            raise ValueError("Query must be a dictionary.")
        AnimalShelter._reject_unsafe_keys(query)
        return query

    @staticmethod
    def _validate_projection(projection: Projection) -> Projection:
        if not isinstance(projection, dict):
            raise ValueError("Projection must be a dictionary.")
        if any(value not in (0, 1, False, True) for value in projection.values()):
            raise ValueError("Projection values must be 0/1 or boolean.")
        AnimalShelter._reject_unsafe_keys(projection)
        return projection

    @staticmethod
    def _validate_update_values(update_values: Document) -> Document:
        if not isinstance(update_values, dict) or not update_values:
            raise ValueError("Update values must be a non-empty dictionary.")
        if any(str(key).startswith("$") for key in update_values):
            raise ValueError("Update values must be field names only; operators are applied by the repository.")
        AnimalShelter._reject_unsafe_keys(update_values)
        return update_values

    @staticmethod
    def _reject_unsafe_keys(document: Mapping[str, Any]) -> None:
        """Reject unsafe MongoDB operators such as $where in user-provided input."""
        blocked = {"$where", "$function", "$accumulator"}
        for key, value in document.items():
            if key in blocked:
                raise ValueError(f"Unsafe MongoDB operator is not allowed: {key}.")
            if isinstance(value, Mapping):
                AnimalShelter._reject_unsafe_keys(value)
            elif isinstance(value, Iterable) and not isinstance(value, (str, bytes)):
                for item in value:
                    if isinstance(item, Mapping):
                        AnimalShelter._reject_unsafe_keys(item)


class CRUD(AnimalShelter):
    """Backward-compatible class name used by the original dashboard notebook."""
