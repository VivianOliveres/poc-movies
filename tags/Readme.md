# Tags

This project aims to manage user tagging of movies.
It allows retrieving, adding and updating (CRUD) tags based on a WebService.

## Rating descriptor

Each tag is defined by an *movieId* id (long), a *userId* id (long), a *tagName* (String) and 
a *tagTime* (LocalDateTime).

    {
        "movieId": {id},
        "userId": {userId}, 
        "tagName": {ratingValue},
        "tagTime": {ratingTime}
    }

## Operations

| Method | Endpoint                                          | Input Json                     | Output Json                    | Description |
| ------ | ------------------------------------------------- | ------------------------------ | ------------------------------ | ----------- |
| GET    | /tags/movie/{movieId}                             |                                | List of Tags                   | Get all tags by movieId |
| GET    | /tags/movie/{movieId}/user/{userId}               |                                | Tags                           | Get tags by movieId and userId |
| GET    | /tags/user/{userId}                               |                                | List of Tags                   | Get all tags by userId |
| GET    | /tags/user/{userId}/movie/{movieId}               |                                | List of Tags                   | Get one tag by movieId and userId (same as */tags/movie/{movieId}/user/{userId} *) |
| POST   | /tags                                             | Tag                            |                                | POST a json tag |
| POST   | /tags/bulk                                        | List of Tags                   |                                | POST multiple json tags |
| POST   | /tags/user/{userId}/movie/{movieId}/{tagName}     |                                |                                | POST a simple tag with timestamp set to now |

## Run service

From root project:

    ./gradlew tags:bootRun

