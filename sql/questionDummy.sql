USE detalks;

DROP PROCEDURE InsertExampleQuestions;

CREATE PROCEDURE InsertExampleQuestions()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE member_id BIGINT;
    DECLARE question_title VARCHAR(30);
    DECLARE question_content VARCHAR(255);
    DECLARE is_solved BIT(1);
    DECLARE question_rep INT;
    DECLARE question_state BIT(1);
    DECLARE q_view_count INT;
    DECLARE q_vote_count INT;

    WHILE i <= 10 DO
        SET member_id = FLOOR(1 + RAND() * 10); -- 임의의 회원 ID (1부터 10까지)
        SET question_title = CONCAT('Question Title ', i);
        SET question_content = CONCAT('This is the content of question number ', i, '.');
        SET is_solved = IF(RAND() < 0.5, b'0', b'1'); -- 랜덤하게 해결됨/안됨 결정
        SET question_rep = FLOOR(1 + RAND() * 100);
        SET question_state = b'1';
        SET q_view_count = FLOOR(1 + RAND() * 100);
        SET q_vote_count = FLOOR(1 + RAND() * 100);

        INSERT INTO `questions` (
            `q_created_at`, `is_solved`, `q_modified_at`, `question_content`, 
            `question_rep`, `question_state`, `question_title`, `q_view_count`, 
            `q_vote_count`, `member_idx`
        ) VALUES (
            CURRENT_TIMESTAMP, is_solved, CURRENT_TIMESTAMP, question_content, 
            question_rep, question_state, question_title, q_view_count, 
            q_vote_count, member_id
        );

        SET i = i + 1;
    END WHILE;
END //

CALL InsertExampleQuestions();

SELECT * FROM questions;
