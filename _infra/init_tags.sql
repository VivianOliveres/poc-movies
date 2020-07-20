USE tags;

CREATE TABLE IF NOT EXISTS User_Tag (
    user_tag_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    movie_id BIGINT NOT NULL,
    tag_name_id BIGINT NOT NULL,
    tag_time DATETIME NOT NULL
);
ALTER TABLE User_Tag ADD UNIQUE user_tag_unique_index(user_id, movie_id, tag_name_id);

CREATE TABLE IF NOT EXISTS Tag_Name (
    tag_name_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tag_name VARCHAR(50) NOT NULL
);
ALTER TABLE Tag_Name ADD UNIQUE INDEX tag_name_unique (tag_name DESC) ;

CREATE TABLE IF NOT EXISTS User_Tag_Name (
    user_tags_name_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_tag_id BIGINT NOT NULL,
    tag_name_id BIGINT NOT NULL,
    CONSTRAINT fk_user_tag_id FOREIGN KEY (user_tag_id) REFERENCES User_Tag(user_tag_id),
    CONSTRAINT fk_tag_name_id FOREIGN KEY (tag_name_id) REFERENCES Tag_Name(tag_name_id)
);
ALTER TABLE User_Tag_Name ADD UNIQUE user_tag_name_unique_index(user_tag_id, tag_name_id);

