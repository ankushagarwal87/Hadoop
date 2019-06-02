Mysql commands 
show databases;
create database test;
use test;
show tables;



CREATE  TABLE `test`.`review` (
  `category` VARCHAR(100) NOT NULL ,
  `url` VARCHAR(200) NULL ,
  `positive` INT NULL ,
  `negative` INT NULL ,
  `total` INT NULL ,
  `hash` VARCHAR(45) NULL 
  );


ALTER TABLE `test`.`review`
    ADD id INT UNSIGNED NOT NULL AUTO_INCREMENT FIRST,
    ADD PRIMARY KEY (id);



CREATE  TABLE `test`.`review_stg` (
  `category` VARCHAR(100) NOT NULL ,
  `url` VARCHAR(200) NULL ,
  `positive` INT NULL ,
  `negative` INT NULL ,
  `total` INT NULL ,
  `hash` VARCHAR(45) NULL 
  );


INSERT INTO `test`.`review` (category, url, positive, negative, total , hash, version )
 SELECT category, url, positive, negative, total,  hash  , '0' FROM `test`.`review_stg`
