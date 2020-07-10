# Data Loader

This project aims to POST data to different services in order to initialize them.

It is currently using these loaders:
* Movie Loader


## Movie Loader

This loader will feed the `inventory` module based on the *movies.csv* file from [MovieLens](https://grouplens.org/datasets/movielens/20m).

It will perform a bulk POST to the service. Example of call:

    # Create jar
    ./gradlew build

    # Start it
    java -jar data-loader/build/libs/data-loader-1.0.jar movies.csv http://localhost:8080/inventory/movies
