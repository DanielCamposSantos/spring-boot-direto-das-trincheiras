SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS user_profile;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS profile;

CREATE TABLE profile (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         description VARCHAR(255) NOT NULL,
                         name VARCHAR(255) NOT NULL,
                         PRIMARY KEY (id),
                         UNIQUE (name)
);

CREATE TABLE user (
                      id BIGINT NOT NULL AUTO_INCREMENT,
                      email VARCHAR(255) NOT NULL,
                      first_name VARCHAR(255) NOT NULL,
                      last_name VARCHAR(255) NOT NULL,
                      PRIMARY KEY (id),
                      UNIQUE (email)
);

CREATE TABLE user_profile (
                              id BIGINT NOT NULL AUTO_INCREMENT,
                              profile_id BIGINT NOT NULL,
                              user_id BIGINT NOT NULL,
                              PRIMARY KEY (id),
                              FOREIGN KEY (profile_id) REFERENCES profile(id),
                              FOREIGN KEY (user_id) REFERENCES user(id)
);

SET FOREIGN_KEY_CHECKS = 1;