CREATE DATABASE dbname;
CREATE TABLE `dbname`.`Users` (`userId` INT NOT NULL,`userLogin` VARCHAR(45) NULL,`userPassword` VARCHAR(45) NOT NULL,`isBlocked` TINYINT (45) NOT NULL, PRIMARY KEY (`userId`));
CREATE TABLE `dbname`.`Products` (`productId` INT NOT NULL,`productTitle` VARCHAR(45) NOT NULL,`productQuantity` VARCHAR(45) NULL,`productPrice` DECIMAL(6,2) NOT NULL,PRIMARY KEY (`productId`));