/***************************************************************************
* Create the database named chat_room, its tables, and a user
*****************************************************************************/

DROP DATABASE IF EXISTS chat_room;
CREATE DATABASE chat_room;
USE chat_room;

/*Tables*/

CREATE TABLE ChatRoomUser (
		username VARCHAR(50) NOT NULL, 
        first_name VARCHAR(40) NOT NULL, 
        last_name VARCHAR(40) NOT NULL, 
        user_role VARCHAR(20) NOT NULL,
		CONSTRAINT PK_username PRIMARY KEY(username)
);

CREATE TABLE UserCredentials (
		username VARCHAR(50) NOT NULL, 
        password VARCHAR(60) NOT NULL,
        FOREIGN KEY (username) 
		REFERENCES ChatRoomUser (username),
		CONSTRAINT PK_username PRIMARY KEY(username)
);

CREATE TABLE ChatRoom (
		room_name VARCHAR(50) NOT NULL, 
        time_created DATETIME NOT NULL, 
        room_creator VARCHAR(50) NOT NULL, 
        FOREIGN KEY (room_creator) 
		REFERENCES ChatRoomUser (username),
		CONSTRAINT PK_id PRIMARY KEY(room_name)
);

CREATE TABLE Message (
		id BIGINT NOT NULL AUTO_INCREMENT, 
        time_written DATETIME NOT NULL, 
        message VARCHAR(1000) NOT NULL, 
        image TINYINT(1) NOT NULL, 
        video TINYINT(1) NOT NULL, 
        username VARCHAR(50) NOT NULL, 
        room_name VARCHAR(50) NOT NULL, 
        FOREIGN KEY (username) 
		REFERENCES ChatRoomUser (username),
        FOREIGN KEY (room_name) 
		REFERENCES ChatRoom (room_name),
		CONSTRAINT PK_id PRIMARY KEY(id)
);

CREATE TABLE UserSignIn (
		username VARCHAR(50) NOT NULL, 
        time_signed_in DATETIME NOT NULL, 
        FOREIGN KEY (username) 
		REFERENCES ChatRoomUser (username),
		CONSTRAINT PK_username_time_signed_in PRIMARY KEY(username, time_signed_in)
);

CREATE TABLE UserSignOut (
		username VARCHAR(50) NOT NULL, 
        time_signed_out DATETIME NOT NULL, 
        FOREIGN KEY (username) 
		REFERENCES ChatRoomUser (username),
		CONSTRAINT PK_username_time_signed_out PRIMARY KEY(username, time_signed_out)
);

CREATE TABLE UserAccount (
		username VARCHAR(50) NOT NULL, 
        email_address VARCHAR(255) NOT NULL UNIQUE, 
        registration_code VARCHAR(64) NOT NULL UNIQUE, 
        activated TINYINT(1) NOT NULL, 
        FOREIGN KEY (username) 
		REFERENCES ChatRoomUser (username),
		CONSTRAINT PK_username PRIMARY KEY(username)
);

-- Create a procedure for generating usernames for new ChatRoomUser entities

DELIMITER //
CREATE PROCEDURE generateUsername
	(IN first_name_in VARCHAR(40), OUT username VARCHAR(50))
BEGIN
	DECLARE name_exists TINYINT(1) DEFAULT 0;
    DECLARE counter INT DEFAULT 0;
	DECLARE new_name VARCHAR(50);
    
    START TRANSACTION;
    
    SET first_name_in = REPLACE(first_name_in, ' ', '');
    
    name_generator: LOOP
		
        SET counter  = counter + 1;
        SET new_name = CONCAT(LOWER(first_name_in), counter);        
        SELECT COUNT(*) INTO name_exists 
			FROM ChatRoomUser u 
			WHERE LOWER(u.username) = new_name;
        IF name_exists = 0 THEN
			LEAVE name_generator;
		END IF;
		
    END LOOP name_generator;
    
    SET username = new_name;
    
COMMIT;
END ; //
DELIMITER ;

-- Create chat_room_db_user and grant privileges

DELIMITER //
CREATE PROCEDURE drop_user_if_exists()
	BEGIN
	DECLARE userCount BIGINT DEFAULT 0 ;
	SELECT COUNT(*) INTO userCount FROM mysql.user
		WHERE User = 'chat_room_db_user' and  Host = 'localhost';

	IF userCount > 0 THEN
        DROP USER chat_room_db_user@localhost;
	END IF;
	END ; //
DELIMITER ;
CALL drop_user_if_exists() ;
CREATE USER chat_room_db_user@localhost IDENTIFIED BY '8B2R0li!dS@x26{';
GRANT EXECUTE, SELECT, INSERT, UPDATE, DELETE, CREATE, DROP
ON chat_room.*
TO chat_room_db_user@localhost;
GRANT SELECT ON mysql.proc TO chat_room_db_user@localhost;

INSERT INTO ChatRoomUser (username, first_name, last_name, user_role) 
VALUES ('fred1', 'Fred', 'Flintstone', 'admin'),
('wilma1', 'Wilma', 'Flintstone', 'user'),
('barney1', 'Barney', 'Rubble', 'user'),
('betty1', 'Betty', 'Rubble', 'user');

INSERT INTO UserCredentials (username, password) 
VALUES ('fred1', '$2a$12$j4JGuOEVcmcP1nR3wldNk.ePU3Ef1qyR3UHN9e7VJKqc5lzjYE1hq'), 
('wilma1', '$2a$12$9m4XdxJoXVci1E9KYZNh/ORlmQggOqTAydXIylxt5ABPXl.qPbm.6'),
 ('barney1', '$2a$12$ndAXal7/E8//DJ8jo2wivuwDmbQ2XJU.cEaGdWpXqdkHVey6aEDJ2'),
 ('betty1', '$2a$12$GyilNYs8z.RovJ7TPCXf7eMTAnQcsU47MNB2yD7tXoVt/MX6XJw8e');
 
 INSERT INTO UserAccount (username, email_address, registration_code, activated) 
VALUES ('fred1', 'fred123@hotmail.com', '$2a$12$8.OezcuNgo0/OmBkDs5zsueb1sXaU8OrrRZDMMq9o1MUgoxIl3ISO', 1), 
('wilma1', 'wilma123@gmail.com', '$2a$12$hV828c1PfWCb1x9XjoX4GONVDwdgtsln65tI5At351Ztl5qzr9x7y', 1),
 ('barney1', 'barney123@yahoo.com', '$2a$12$CZSvD2UfGpz/JzskCZ4k3uH/tJpM.rZRB39MeJimqYx4aW19FfbbW', 1),
 ('betty1', 'betty123@gmx.com', '$2a$12$BsMVSjdUINlWshZJCQGlD./l4YKmdDIsDFtcAaCIbbvZE9kXh/JBS', 1);