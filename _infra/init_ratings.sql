USE ratings;

CREATE TABLE IF NOT EXISTS Ratings (
    rating_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    value DOUBLE NOT NULL,
    ratingTime DATETIME NOT NULL
);
ALTER TABLE Ratings ADD UNIQUE ratings_unique_index(movie_id, user_id);
