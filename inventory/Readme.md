# Inventory

This project aims to manage the catalog of movies available in the platform.
It allows retrieving, adding and updating (CRUD) movies based on a WebService.

## Movie descriptor

Each movie is defined by an *id* (long value), a *title* (String value, case-sensitive)
and *categories* (list of Strings case-insensitive).

    {
        "id": {id},
        "title": {title}, 
        "categories": [{categoryName1}, ..., {categoryNameN}]
    }

## Operations

| Method | Endpoint              | Input Json                 | Output Json               | Description |
| ------ | --------------------- | -------------------------- | ------------------------- | ----------- |
| GET    | /inventory/movie/{id} |                            | Movie descriptor          | Get a movie by its id |
| POST   | /inventory/movie      | Movie descriptor           |                           | Insert a movie |
| PUT    | /inventory/movie      | Movie descriptor           |                           | Update a movie |
| GET    | /inventory/movies     | List of movie ids          | List of movie descriptors | Get a list of movies by their ids |
| POST   | /inventory/movies     | List of movie descriptors  |                           | Insert movies |

## Run service

From root project:

    ./gradlew inventory:bootRun

