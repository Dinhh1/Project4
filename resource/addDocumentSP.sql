USE `publications`;
DROP procedure IF EXISTS `AddDocument`;

DELIMITER $$
USE `publications`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `AddDocument`(IN title VARCHAR(300), -- 1
							  IN start_page INTEGER, -- 2
							  IN end_page INTEGER, -- 3
							  IN year INTEGER,	 -- 4
							  IN volume INTEGER, -- 5
							  IN number INTEGER, -- 6
							  IN url VARCHAR(200), -- 7
							  IN ee VARCHAR(100), -- 8
							  IN cdrom VARCHAR(75),-- 9
							  IN cite VARCHAR(75), -- 10
							  IN crossref VARCHAR(75), -- 11
					          IN isbn VARCHAR(21),  -- 12 
							  IN series VARCHAR(100), -- 13 
							  IN editor VARCHAR(61), -- 14
							  IN publisher VARCHAR(300), -- 15
							  IN bt VARCHAR(300) CHARACTER SET UTF8, -- 16
							  IN genre VARCHAR(300), -- 17
                              IN authorArray VARCHAR(1000) CHARACTER SET UTF8 -- 18
							  )
proc:BEGIN


	DECLARE people_id int default -1;
	DECLARE publisher_id int default -1;
	DECLARE bk_id int default - 1;
	DECLARE genre_id int default -1;
    
    DECLARE author_id int default -1;
    
    DECLARE doc_id int default - 1;
    
	DECLARE output int default -1;
    
	DECLARE sep varchar(2);
	DECLARE separatorLength INTEGER;
	DECLARE formattedArray varchar(1000);
	DECLARE currentValue varchar(200);
    
	START TRANSACTION;
    
    -- SELECT b.id into bk_id from tbl_booktitle as b where b.title like concat('%',TRIM(bt), '%') limit 1;
    SELECT b.id into bk_id from tbl_booktitle as b where b.title = TRIM(BT) limit 1;
    
    IF bk_id = -1 AND bt is not NULL THEN
		INSERT INTO tbl_booktitle(title) values(TRIM(bt));
		SET bk_id = LAST_INSERT_ID();
	END IF;
    
    IF bt is null then
		set bk_id = NULL;
	END IF;
    
	
	IF editor IS NULL THEN
		set people_id = NULL;
	ELSE
		SELECT e.id into people_id from tbl_people as e where e.name = editor;

		-- query people
		IF people_id = -1 THEN
			INSERT INTO tbl_people(name) VALUES(TRIM(editor));
			SET people_id = LAST_INSERT_ID();
		END IF;

		IF people_id = -1 THEN
			SET people_id = NULL;
		END IF;
	END IF;


	IF publisher IS NULL THEN
		set publisher_id = NULL;
	ELSE
		SELECT x.id into publisher_id from tbl_publisher as x where x.publisher_name = publisher limit 1;

		-- query publisher
		IF publisher_id = -1 THEN
			INSERT INTO tbl_publisher(publisher_name) VALUES(publisher);
			SET publisher_id = LAST_INSERT_ID();
		END IF;

		IF publisher_id = -1 THEN
			SET publisher_id = NULL;
		END IF;
	END IF;


	IF genre is NULL THEN
		SET genre_id = NULL;
	ELSE
		SELECT g.id into genre_id from tbl_genres as g where g.genre_name = TRIM(genre) limit 1;
		-- query genre id
		If genre_id = -1 THEN
			INSERT INTO tbl_genres(genre_name) VALUES(TRIM(genre));
			SET genre_id = LAST_INSERT_ID();
		END IF; 

		IF genre_id = -1 THEN
			set genre_id = NULL;
		END IF;
	END IF;


	-- null if we 

	INSERT INTO tbl_dblp_document(title, 
								  start_page, 
								  end_page, 
								  year, 
								  volume, 
								  number, 
								  url, 
								  ee, 
								  cdrom, 
								  cite, 
								  crossref, 
								  isbn, 
								  series, 
								  editor_id,
								  booktitle_id,
								  genre_id,
								  publisher_id
								  )
							VALUES(title,
								  start_page, 
								  end_page, 
								  year, 
								  volume, 
								  number, 
								  url, 
								  ee, 
								  cdrom, 
								  cite, 
								  crossref, 
								  isbn, 
								  series, 
								  people_id,
								  bk_id,
								  genre_id,
								  publisher_id
                                );
                                
	SET doc_id = LAST_INSERT_ID();

	IF doc_id = -1 THEN
		ROLLBACK;
		LEAVE proc;
	END IF;
    
    -- handle the authors
	IF authorArray is NOT NULL THEN
		SET formattedArray = authorArray;
		SET sep = ',';
		SET separatorLength = CHAR_LENGTH(sep);
		
		
		WHILE formattedArray != '' > 0 DO
			SET output = -1;
			SET currentValue = SUBSTRING_INDEX(formattedArray, sep, 1);
			
			SELECT p.id into author_id from tbl_people as p where p.name = TRIM(currentValue);
			
			IF author_id = -1 THEN
				INSERT INTO tbl_people(name) values(TRIM(currentValue));
				SET author_id = LAST_INSERT_ID();
			END IF;
            
			INSERT INTO tbl_author_document_mapping(doc_id, author_id) VALUES(doc_id, author_id);
			SET output = LAST_INSERT_ID();
				

			SET formattedArray = SUBSTRING(formattedArray, CHAR_LENGTH(currentValue) + separatorLength + 1);
		END WHILE;
	END IF;
    
	COMMIT;
    
END$$

DELIMITER ;

