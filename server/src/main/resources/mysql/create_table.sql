SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS Account
;

 
CREATE TABLE Account
(
	id BIGINT NOT NULL,
	userName VARCHAR(200),
	password VARCHAR(50),
	sessionID VARCHAR(200),
	person_id BIGINT,
	deleted TINYINT DEFAULT 0,
	PRIMARY KEY (id)
) ENGINE=InnoDB
;

CREATE TABLE Profile
(
	id BIGINT NOT NULL,
	realName VARCHAR(200),
	birthday VARCHAR(200),
	school VARCHAR(200),
	currentPlace VARCHAR(200),
	person_id BIGINT,
	PRIMARY KEY (id)
) ENGINE=InnoDB
;

CREATE TABLE Person
(
	id BIGINT NOT NULL,
	lastPersonalLocation VARCHAR(200),
	accountId BIGINT,
	profileId BIGINT,
	PRIMARY KEY (id)
) ENGINE=InnoDB
;

CREATE TABLE RichMan
(
	id BIGINT NOT NULL,
	personID BIGINT,
	money BIGINT,
	PRIMARY KEY (id)
) ENGINE=InnoDB
;

SET FOREIGN_KEY_CHECKS=1;
