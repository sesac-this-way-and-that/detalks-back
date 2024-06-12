CREATE USER 'sesac'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON *.* TO 'sesac'@'localhost';
SELECT User, Host FROM mysql.user;

drop database detalks;
create database detalks character set utf8mb4 collate utf8mb4_unicode_ci;
show databases;
use detalks;

SHOW TABLES;


DROP TABLE users;
DROP TABLE question_tags;
DROP TABLE answer_votes;
DROP TABLE tags;
DROP TABLE question_votes;
DROP TABLE answer_votes;
DROP TABLE questions;
DROP TABLE answers;
DROP TABLE bookmarks;


SELECT * FROM members;
SELECT * FROM questions;
SELECT * FROM question_tags;
SELECT * FROM tags;
SELECT * FROM answers;
SELECT * FROM answer_votes;
