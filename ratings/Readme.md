# Ratings

This project aims to manage user ratings of movies.
It allows retrieving, adding and updating (CRUD) ratings ids based on a WebService.

## Rating descriptor

Each ratings is defined by an *movieId* id (long), a *userId* id (long), a *ratingValue* (double) and 
a *ratingTime* (LocalDateTime).

    {
        "movieId": {id},
        "userId": {userId}, 
        "ratingValue": {ratingValue},
        "ratingTime": {ratingTime}
    }

## Operations

| Method | Endpoint                                             | Input Json                     | Output Json                    | Description |
| ------ | ---------------------------------------------------- | ------------------------------ | ------------------------------ | ----------- |
| GET    | /ratings/movie/{movieId}                             |                                | List of Ratings                | Get all ratings by movieId |
| GET    | /ratings/movie/{movieId}/user/{userId}               |                                | Rating                         | Get one rating by its movieId and userId |
| GET    | /ratings/user/{userId}                               |                                | List of Ratings                | Get all ratings by userId |
| GET    | /ratings/user/{userId}/movie/{movieId}               |                                | List of Ratings                | Get one rating by its movieId and userId (same as */ratings/movie/{movieId}/user/{userId} *) |
| POST   | /ratings                                             | Rating                         |                                | POST a json rating |
| POST   | /ratings/bulk                                        | List of Ratings                |                                | POST multiple json ratings |
| POST   | /ratings/user/{userId}/movie/{movieId}/{ratingValue} |                                |                                | POST a simple rating |

## Run service

From root project:

    ./gradlew ratings:bootRun

