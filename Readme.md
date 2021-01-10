# POC Movies

This project aims to create a microservice platform in order to allow users to:
* Consult catalog of movies
* Rate movies
* Tag movies
* Access to personalized recommendation  *TODO*

## Structure

Currently, contains following projects:
* Inventory: to manage movie catalog ([Readme](../master/inventory/Readme.md))
* Rate movies ([Readme](../master/ratings/Readme.md))
* Tag movies ([Readme](../master/tags/Readme.md))
* Links movies ([Readme](../master/links/Readme.md))
* DataLoader: to load data from [MovieLens](https://grouplens.org/datasets/movielens/20m) project 

## To build project

    # Start DB
    docker-compose up
    
    # Build
    ./gradlew build
