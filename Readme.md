# POC Movies

This project aims to create a microservice platform in order to allow users to:
* Consult catalog of movies
* Rate movies *TODO*
* Tag movies *TODO*
* Serve recommendation  *TODO*

## Structure

Currently, contains following projects:
* Inventory: to manage movie catalog
* DataLoader: to load data from [MovieLens](https://grouplens.org/datasets/movielens/20m) project 

## To build project

    # Start DB
    docker-compose up
    
    # Build
    ./gradlew build
