DROP DATABASE IF EXISTS `ace_student_management`;
CREATE DATABASE `ace_student_management` CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci';

USE `ace_student_management`;

CREATE TABLE `user` (
    `id_code` INT UNIQUE NOT NULL AUTO_INCREMENT,
    `id_prefix` VARCHAR(10) NOT NULL,
    `name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(255) UNIQUE NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `role` ENUM('ADMIN', 'USER') NOT NULL,
    `approved` BOOLEAN NOT NULL DEFAULT false,
    PRIMARY KEY(`id_code`, `id_prefix`)
);

CREATE TABLE `course` (
    `id_prefix` VARCHAR(10) NOT NULL,
    `id_code` INT,
    `name` VARCHAR(255) UNIQUE NOT NULL,
    `short_name` VARCHAR(10) NOT NULL,
    PRIMARY KEY(`id`)
);

CREATE TABLE `student` (
    `id_code` INT UNIQUE NOT NULL AUTO_INCREMENT,
    `id_prefix` VARCHAR(10) NOT NULL,
    `name` VARCHAR(100) NOT NULL,
    `date_of_birth` DATE NOT NULL,
    `gender` ENUM('MALE', 'FEMALE') NOT NULL,
    `phone` VARCHAR(15) NOT NULL,
    `education` VARCHAR(255) NOT NULL,
    PRIMARY KEY(`id_code`, `id_prefix`)
);

CREATE TABLE `student_course` (
    `student_id` INT NOT NULL,
    `course_id` INT NOT NULL,
    UNIQUE (`student_id`, `course_id`),
    FOREIGN KEY(`student_id`) REFERENCES `student`(`id_code`) ON DELETE CASCADE,
    FOREIGN KEY(`course_id`) REFERENCES `course`(`id_code`),
    PRIMARY KEY(`student_id`, `course_id`)
);

INSERT INTO `user` (`id_prefix`, `name`, `email`, `password`, `approved`) VALUES ('USR', 'Admin', 'admin@gmail.com', 'admin', true);