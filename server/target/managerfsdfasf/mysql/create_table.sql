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
) ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci
;

CREATE TABLE Profile
(
	id BIGINT NOT NULL,
	realName VARCHAR(200),
	birthday VARCHAR(200),
	school VARCHAR(200),
	currentPlace VARCHAR(200),
	person_id BIGINT,
	avatar Blob,
	PRIMARY KEY (id)
) ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci
;

CREATE TABLE Person
(
	id BIGINT NOT NULL,
	lastPersonalLocation BIGINT,
	accountId BIGINT,
	profileId BIGINT,
	mobile varchar(50), 
	friendIds VARCHAR(8000),
	topPlaceIds VARCHAR(8000),
	PRIMARY KEY (id)
) ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci
;

CREATE TABLE RichMan
(
	id BIGINT NOT NULL,
	personID BIGINT,
	money BIGINT,
	PRIMARY KEY (id)
) ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci
;

CREATE TABLE PersonalLocation
(
	id BIGINT NOT NULL,
	ownerId BIGINT NOT NULL,
	accountId BIGINT,
	latitude DECIMAL,
	longitude DECIMAL,
	time TIMESTAMP,
	PRIMARY KEY (id)
) ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci
;

CREATE TABLE FriendRequest
(
	id BIGINT NOT NULL,
	targetPersonId BIGINT NOT NULL,
	sourcePersonId BIGINT  NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci
;

CREATE TABLE Place
(
	id BIGINT NOT NULL,
	placeName VARCHAR(200),
	latitude DECIMAL,
	longitude DECIMAL,
	creatorId BIGINT,
	topUserId BIGINT,
	parentId BIGINT,
	checkInTimes BIGINT,
	currentMoney BIGINT,
	image blob,
	create_Time TIMESTAMP default CURRENT_TIMESTAMP,
	PRIMARY KEY (id)
) ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci
;

CREATE TABLE Reply
(
	id BIGINT NOT NULL,
	ownerId BIGINT NOT NULL,
	content VARCHAR(5000),
	date TIMESTAMP default CURRENT_TIMESTAMP,
	palceId BIGINT,
	PRIMARY KEY (id)
) ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci
;

CREATE TABLE CheckInCounter
(
	id BIGINT NOT NULL,
	ownerId BIGINT NOT NULL,
	placeId BIGINT NOT NULL,
	counter INT DEFAULT 0,
	PRIMARY KEY (id)
) ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci
;

CREATE TABLE CheckIn
(
	id BIGINT NOT NULL,
	ownerId BIGINT NOT NULL,
	placeId BIGINT NOT NULL,
	time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (id)
) ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci
;

CREATE TABLE OAuthAccessKey
(
	id BIGINT NOT NULL,
	ownerId BIGINT NOT NULL,
	accessKey VARCHAR(200),
	accessKeySecret VARCHAR(200),	
	provider VARCHAR(200),	
	PRIMARY KEY (id)
) ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci
;

CREATE TABLE RenRen
(
	id BIGINT NOT NULL,
	ownerId BIGINT NOT NULL,
	userName VARCHAR(200),
	password VARCHAR(200),	
	PRIMARY KEY (id)
) ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci
;

CREATE TABLE UserLocation
(
  id BIGINT NOT NULL,
  ownerid BIGINT,          
  mobile varchar(50),
  address varchar(200),               
  timestamp timestamp DEFAULT CURRENT_TIMESTAMP,         
  city varchar(50),           
  street varchar(200),          
  latitude varchar(20),         
  longitude varchar(20),
  INDEX idx_gps_userid (id ASC)
) ENGINE=InnoDB CHARACTER SET utf8 COLLATE utf8_general_ci
;

SET FOREIGN_KEY_CHECKS=1;
