USE detalks;

DROP PROCEDURE IF EXISTS InsertExampleAnswers;

CREATE PROCEDURE InsertExampleAnswers()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE answer_content VARCHAR(255);
    DECLARE a_created_at DATETIME(6);
    DECLARE a_modified_at DATETIME(6);
    DECLARE member_idx BIGINT;
    DECLARE question_id BIGINT;

    WHILE i <= 10 DO
        SET answer_content = CONCAT('Example Answer ', i);
        SET a_created_at = NOW();
        SET a_modified_at = NOW();
        SET member_idx = FLOOR(1 + RAND() * 10); 
        SET question_id = FLOOR(1 + RAND() * 10);

        INSERT INTO `answers` (`answer_content`, `answer_state`, `a_created_at`, `is_selected`, `a_modified_at`, `a_vote_count`, `member_idx`, `question_id`)
        VALUES (answer_content, 1, a_created_at, 0, a_modified_at, 0, member_idx, question_id);

        SET i = i + 1;
    END WHILE;
END //

CALL InsertExampleAnswers();

SELECT * FROM answers;
