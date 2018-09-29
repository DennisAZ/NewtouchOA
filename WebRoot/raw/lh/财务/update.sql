CREATE TABLE `t9`.`SYS_TABLE` (
  `SEQ_ID` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  `TABLE_NAME` VARCHAR(45) NOT NULL,
  `NO_FILED_PRE` VARCHAR(45),
  `NO_FILED_LENGTH` INTEGER UNSIGNED,
  `NO_FILED_START` INTEGER UNSIGNED,
  PRIMARY KEY (`SEQ_ID`)
)
ENGINE = InnoDB;

CREATE TABLE `t9`.`SYS_TABLE_FIELD` (
  `SEQ_ID` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  `FIELD_NAME` VARCHAR(45) NOT NULL,
  `FIELD_TYPE` VARCHAR(45) NOT NULL,
  `FIELD_LENGTH` INTEGER UNSIGNED NOT NULL,
  `TABLE_ID` INTEGER UNSIGNED NOT NULL,
  PRIMARY KEY (`SEQ_ID`)
)
ENGINE = InnoDB;
