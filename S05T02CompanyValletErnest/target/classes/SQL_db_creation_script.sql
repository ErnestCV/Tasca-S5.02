-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema jocdaus
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema jocdaus
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `jocdaus` DEFAULT CHARACTER SET utf8 ;
USE `jocdaus` ;

-- -----------------------------------------------------
-- Table `jocdaus`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jocdaus`.`user` (
  `id_user` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(20) NOT NULL,
  `email` VARCHAR(50) NOT NULL,
  `password` VARCHAR(120) NOT NULL,
  PRIMARY KEY (`id_user`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `username_UNIQUE` ON `jocdaus`.`user` (`username` ASC) VISIBLE;

CREATE UNIQUE INDEX `email_UNIQUE` ON `jocdaus`.`user` (`email` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `jocdaus`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jocdaus`.`roles` (
  `id_role` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id_role`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `role_UNIQUE` ON `jocdaus`.`roles` (`name` ASC) VISIBLE;

INSERT INTO roles (`name`) VALUES ("ROLE_USER");
INSERT INTO roles (`name`) VALUES ("ROLE_ADMIN");

-- -----------------------------------------------------
-- Table `jocdaus`.`games`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jocdaus`.`games` (
  `id_game` INT NOT NULL AUTO_INCREMENT,
  `firstthrow` INT NOT NULL,
  `secondthrow` INT NOT NULL,
  `datetime` VARCHAR(45) NOT NULL,
  `user_id` INT NULL,
  PRIMARY KEY (`id_game`),
  CONSTRAINT `fk_games_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `jocdaus`.`user` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_games_user1_idx` ON `jocdaus`.`games` (`user_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `jocdaus`.`user_has_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jocdaus`.`user_has_roles` (
  `user_id` INT NOT NULL,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`),
  CONSTRAINT `fk_user_has_roles_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `jocdaus`.`user` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_has_roles_roles1`
    FOREIGN KEY (`role_id`)
    REFERENCES `jocdaus`.`roles` (`id_role`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_user_has_roles_roles1_idx` ON `jocdaus`.`user_has_roles` (`role_id` ASC) VISIBLE;

CREATE INDEX `fk_user_has_roles_user_idx` ON `jocdaus`.`user_has_roles` (`user_id` ASC) VISIBLE;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
