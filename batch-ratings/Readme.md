# Batch Links

This project aims to POST user ratings to ratings service and then GET each to them in order to verify
that they have been correctly imported.

This loader use the *ratings.csv* file from [MovieLens](https://grouplens.org/datasets/movielens/20m).

It will perform a bulk POST to the service. Example of call:

    # Create jar
    ./gradlew batch-ratings:build

    # Start it
    java -jar batch-ratings/build/libs/batch-ratings-1.0.jar ratings.csv http://localhost:8083
