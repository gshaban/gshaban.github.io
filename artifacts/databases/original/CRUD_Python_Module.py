"""MongoDB CRUD module for the Austin Animal Center dashboard.

Student: Shaban Ghaith
Course: CS 340 Client/Server Development
"""

from pymongo import MongoClient
from pymongo.errors import PyMongoError


class CRUD:
    """CRUD operations for the AAC animals collection in MongoDB."""

    def __init__(
        self,
        username,
        password,
        host="nv-desktop-services.apporto.com",
        port=31580,
        database="AAC",
        collection="animals",
    ):
        self.username = username
        self.password = password
        self.host = host
        self.port = port
        self.database_name = database
        self.collection_name = collection

        self.client = MongoClient(
            f"mongodb://{username}:{password}@{host}:{port}/{database}?authSource=admin"
        )
        self.database = self.client[database]
        self.collection = self.database[collection]

    def create(self, data):
        """Insert a document into MongoDB and return True when acknowledged."""
        if data is None or not isinstance(data, dict):
            raise ValueError("Data must be a non-empty dictionary.")
        try:
            result = self.collection.insert_one(data)
            return result.acknowledged
        except PyMongoError as err:
            raise RuntimeError(f"Create operation failed: {err}") from err

    def read(self, query, projection=None):
        """Return a list of documents matching the MongoDB query."""
        if query is None:
            query = {}
        if not isinstance(query, dict):
            raise ValueError("Query must be a dictionary.")
        try:
            if projection is None:
                cursor = self.collection.find(query)
            else:
                cursor = self.collection.find(query, projection)
            return list(cursor)
        except PyMongoError as err:
            raise RuntimeError(f"Read operation failed: {err}") from err

    def update(self, query, update_values):
        """Update documents matching the query and return modified count."""
        if not isinstance(query, dict) or not isinstance(update_values, dict):
            raise ValueError("Query and update values must be dictionaries.")
        try:
            result = self.collection.update_many(query, {"$set": update_values})
            return result.modified_count
        except PyMongoError as err:
            raise RuntimeError(f"Update operation failed: {err}") from err

    def delete(self, query):
        """Delete documents matching the query and return deleted count."""
        if not isinstance(query, dict):
            raise ValueError("Query must be a dictionary.")
        try:
            result = self.collection.delete_many(query)
            return result.deleted_count
        except PyMongoError as err:
            raise RuntimeError(f"Delete operation failed: {err}") from err
