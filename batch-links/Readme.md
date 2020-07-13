# Batch Links

This project aims to POST movies links to links service and then GET each to them in order to verify
that they have been correctly imported.

This loader use the *links.csv* file from [MovieLens](https://grouplens.org/datasets/movielens/20m).

It will perform a bulk POST to the service. Example of call:

    # Create jar
    ./gradlew batch-links:build

    # Start it
    java -jar batch-links/build/libs/batch-links-1.0.jar links.csv http://localhost:8082
