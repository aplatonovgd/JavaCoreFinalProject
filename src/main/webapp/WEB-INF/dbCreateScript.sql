CREATE DATABASE dbname;
CREATE TABLE `dbname`.`Users` (`userId` INT NOT NULL AUTO_INCREMENT,`userLogin` VARCHAR(45) NOT NULL,`userPassword` VARCHAR(45) NOT NULL,`isBlocked` TINYINT (45) NOT NULL, PRIMARY KEY (`userId`));
CREATE TABLE `dbname`.`Products` (`productId` INT NOT NULL AUTO_INCREMENT,`productTitle` VARCHAR(45) NOT NULL,`productQuantity` VARCHAR(45) NOT NULL,`productPrice` DECIMAL(6,2) NOT NULL,PRIMARY KEY (`productId`));
CREATE TABLE `dbname`.`Orders` (`orderId` VARCHAR(45) NOT NULL, `userId` INT NOT NULL, `productId` INT NOT NULL, `productQuantity` VARCHAR(45) NOT NULL);