# Links

This project aims to manage the links between movie ids from different sources (MovieLens, Imdb and Tmdb).
It allows retrieving, adding and updating (CRUD) movie ids based on a WebService.

## MovieLinks descriptor

Each links is defined by an *movieId* id (long value), a *imdbId* id (String value) and a *tmdbId* id (long value).

    {
        "movieId": {id},
        "imdbId": {imdbId}, 
        "tmdbId": {tmdbId}
    }

## Operations

| Method | Endpoint                 | Input Json                     | Output Json                    | Description |
| ------ | ------------------------ | ------------------------------ | ------------------------------ | ----------- |
| GET    | /external/link/{id}      |                                | MovieLinks descriptor          | Get a MovieLinks by its movieId |
| GET    | /external/link/{id}/imdb |                                | MovieLinks descriptor          | Get a ImdbId by its movieId |
| GET    | /external/link/{id}/tmdb |                                | MovieLinks descriptor          | Get a TmdbId by its movieId |
| POST   | /external/link           | MovieLinks descriptor          | MovieLinks descriptor          | Insert a MovieLinks |
| PUT    | /external/link           | MovieLinks descriptor          | List of MovieLinks descriptors | Update a MovieLinks |
| DELETE | /external/link           | MovieLinks descriptor          | List of MovieLinks descriptors | Delete a MovieLinks |
| GET    | /external/links          | List of movie ids              | List of MovieLinks descriptors | Get a list of MovieLinks by their ids |
| POST   | /external/links          | List of MovieLinks descriptors | List of MovieLinks descriptors | Insert MovieLinks |
| DELETE | /external/links          | List of MovieLinks descriptors | List of MovieLinks descriptors | Delete MovieLinks |

## Run service

From root project:

    ./gradlew links:bootRun

