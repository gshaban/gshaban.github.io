"""Compatibility wrapper for the enhanced Grazioso Salvare MongoDB repository.

The original CS 340 dashboard imports CRUD from this file. Keeping this wrapper
allows the notebook to use the enhanced database implementation without changing
that public import path.
"""

from animal_shelter import AnimalShelter, CRUD

__all__ = ["AnimalShelter", "CRUD"]
