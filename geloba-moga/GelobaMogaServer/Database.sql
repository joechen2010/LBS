SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `gelobamoga` ;
CREATE SCHEMA IF NOT EXISTS `gelobamoga` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;

-- -----------------------------------------------------
-- Table `gelobamoga`.`Item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `gelobamoga`.`Item` ;

CREATE  TABLE IF NOT EXISTS `gelobamoga`.`Item` (
  `item_id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`item_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gelobamoga`.`ItemProperty`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `gelobamoga`.`ItemProperty` ;

CREATE  TABLE IF NOT EXISTS `gelobamoga`.`ItemProperty` (
  `ip_id` INT NOT NULL AUTO_INCREMENT ,
  `key` VARCHAR(45) NOT NULL ,
  `string_property` TEXT NULL ,
  `int_property` INT NULL ,
  `float_property` FLOAT NULL ,
  `super_property_fk` INT NULL ,
  `item_fk` INT NULL ,
  PRIMARY KEY (`ip_id`) ,
  INDEX `has_super_property` (`super_property_fk` ASC) ,
  INDEX `has_item` (`item_fk` ASC) ,
  INDEX `has_key` (`key` ASC) ,
  UNIQUE INDEX `i_k_unique` (`item_fk` ASC, `key` ASC) ,
  CONSTRAINT `has_super_property`
    FOREIGN KEY (`super_property_fk` )
    REFERENCES `gelobamoga`.`ItemProperty` (`ip_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `has_item`
    FOREIGN KEY (`item_fk` )
    REFERENCES `gelobamoga`.`Item` (`item_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gelobamoga`.`MapablePosition`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `gelobamoga`.`MapablePosition` ;

CREATE  TABLE IF NOT EXISTS `gelobamoga`.`MapablePosition` (
  `mp_id` INT NOT NULL AUTO_INCREMENT ,
  `latitude` FLOAT NOT NULL ,
  `longitude` FLOAT NOT NULL ,
  `timestamp` DATE NOT NULL ,
  `mapable_fk` INT NOT NULL ,
  PRIMARY KEY (`mp_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gelobamoga`.`MapableItem`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `gelobamoga`.`MapableItem` ;

CREATE  TABLE IF NOT EXISTS `gelobamoga`.`MapableItem` (
  `mi_id` INT NOT NULL AUTO_INCREMENT ,
  `position_fk` INT NOT NULL ,
  `item_fk` INT NOT NULL ,
  PRIMARY KEY (`mi_id`) ,
  INDEX `current_positon` (`position_fk` ASC) ,
  INDEX `is_item` (`item_fk` ASC) ,
  CONSTRAINT `current_positon`
    FOREIGN KEY (`position_fk` )
    REFERENCES `gelobamoga`.`MapablePosition` (`mp_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `is_item`
    FOREIGN KEY (`item_fk` )
    REFERENCES `gelobamoga`.`Item` (`item_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gelobamoga`.`User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `gelobamoga`.`User` ;

CREATE  TABLE IF NOT EXISTS `gelobamoga`.`User` (
  `user_id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `password` VARCHAR(45) NOT NULL ,
  `mapable_fk` INT NOT NULL ,
  PRIMARY KEY (`user_id`) ,
  INDEX `is_mapable` (`mapable_fk` ASC) ,
  CONSTRAINT `is_mapable`
    FOREIGN KEY (`mapable_fk` )
    REFERENCES `gelobamoga`.`MapableItem` (`mi_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
PACK_KEYS = DEFAULT;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

