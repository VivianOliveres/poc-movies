# Batch Inventory

This project aims to POST movies to inventory service and then GET each to them in order to verify
that they have been correctly imported.

This loader use the *movies.csv* file from [MovieLens](https://grouplens.org/datasets/movielens/20m).

It will perform a bulk POST to the service. Example of call:

    # Create jar
    ./gradlew batch-inventory:build

    # Start it
    java -jar batch-inventory/build/libs/batch-inventory-1.0.jar movies.csv http://localhost:8081
