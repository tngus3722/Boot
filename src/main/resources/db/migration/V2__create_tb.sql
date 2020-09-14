
CREATE TABLE IF NOT EXISTS fishing_hole(
                             id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(40),
                             category VARCHAR(40),
                             address VARCHAR(80),
                             latitude DECIMAL(12,10) UNSIGNED NOT NULL,
                             longitude DECIMAL(13,10) UNSIGNED NOT NULL,
                             fish_species VARCHAR(60),
                             date DATE
)default character set utf8 collate utf8_general_ci;

CREATE TABLE IF NOT EXISTS user(
	id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL unique,
    password VARCHAR(70) NOT NULL
);

CREATE TABLE IF NOT EXISTS review(
	id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    fish_id BIGINT UNSIGNED NOT NULL,
    user_id BIGINT UNSIGNED NOT NULL,
    writer VARCHAR(20) NOT NULL,
    title VARCHAR(60) NOT NULL,
    content TEXT(100) NOT NULL,
    created_at DATETIME DEFAULT current_timestamp(),
    update_at DATETIME,
    FOREIGN KEY(fish_id) REFERENCES fishing_hole(id),
    FOREIGN KEY(user_id) REFERENCES user(id)
)default character set utf8 collate utf8_general_ci;