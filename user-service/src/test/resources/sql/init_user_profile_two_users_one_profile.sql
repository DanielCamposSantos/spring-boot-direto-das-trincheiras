DELETE FROM user_profile;
DELETE FROM `user`;
DELETE FROM profile;

ALTER TABLE user_profile AUTO_INCREMENT = 1;
ALTER TABLE `user` AUTO_INCREMENT = 1;
ALTER TABLE profile AUTO_INCREMENT = 1;

insert into `user` (first_name,last_name,email,roles,password) values ('carlos','almeida','carlosalmeida@email.com','USER','{bcrypt}$2a$10$3Sca0Z83afNzOG4BP5uPzeAiSt5X2hh4tf9hWSxXXw3.4WeSOgaLu');
insert into `user` (first_name,last_name,email,roles,password) values ('paulo','alvida','pauloalvida@email.com','USER','{bcrypt}$2a$10$3Sca0Z83afNzOG4BP5uPzeAiSt5X2hh4tf9hWSxXXw3.4WeSOgaLu');
insert into profile (name ,description) values ('Tester', 'Application Tester');
insert into user_profile (user_id, profile_id) values (1,1);
insert into user_profile (user_id, profile_id) values (2,1);