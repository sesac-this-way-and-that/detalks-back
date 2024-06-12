USE detalks;

DROP PROCEDURE InsertExampleQuestionTags;

CREATE PROCEDURE InsertExampleQuestionTags()
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE v_question_id BIGINT;
    DECLARE v_tag_id BIGINT;

    WHILE i < 10 DO
        SET v_question_id = FLOOR(RAND() * 10) + 1; -- 1 이상 10 이하의 랜덤한 수
        SET v_tag_id = FLOOR(RAND() * 10) + 1; -- 1 이상 10 이하의 랜덤한 수

        INSERT INTO `question_tags` (`question_id`, `tag_id`)
        VALUES (v_question_id, v_tag_id);

        SET i = i + 1;
    END WHILE;
END //

CALL InsertExampleQuestionTags();

SELECT * FROM question_tags;
