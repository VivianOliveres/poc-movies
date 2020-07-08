USE inventory;

CREATE TABLE IF NOT EXISTS Movies (
    movie_id BIGINT PRIMARY KEY,
    title TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Categories (
    category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS Movies_Categories (
    movies_categories_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    CONSTRAINT fk_movies_categories_to_movie_id FOREIGN KEY (movie_id) REFERENCES Movies(movie_id),
    CONSTRAINT fk_movies_categories_to_category_id FOREIGN KEY (category_id) REFERENCES Categories(category_id)
);
ALTER TABLE Movies_Categories ADD UNIQUE movies_categories_unique_index(movie_id, category_id);

