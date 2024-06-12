USE detalks;

DROP PROCEDURE InsertExampleTags;

CREATE PROCEDURE InsertExampleTags()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE tag_name VARCHAR(255);

    WHILE i <= 10 DO
        CASE i
            WHEN 1 THEN SET tag_name = 'JavaScript';
            WHEN 2 THEN SET tag_name = 'Python';
            WHEN 3 THEN SET tag_name = 'Java';
            WHEN 4 THEN SET tag_name = 'C#';
            WHEN 5 THEN SET tag_name = 'Ruby';
            WHEN 6 THEN SET tag_name = 'PHP';
            WHEN 7 THEN SET tag_name = 'C++';
            WHEN 8 THEN SET tag_name = 'Swift';
            WHEN 9 THEN SET tag_name = 'Go';
            WHEN 10 THEN SET tag_name = 'Kotlin';
            ELSE SET tag_name = 'Unknown';
        END CASE;

        INSERT INTO `tags` (`tag_name`) VALUES (tag_name);

        SET i = i + 1;
    END WHILE;
END //

CALL InsertExampleTags();

SELECT * FROM tags;