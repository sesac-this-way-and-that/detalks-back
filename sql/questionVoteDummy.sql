USE detalks;

DROP PROCEDURE InsertExampleQuestionVotes;

CREATE PROCEDURE InsertExampleQuestionVotes()
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE v_member_idx BIGINT;
    DECLARE v_question_id BIGINT;
    DECLARE v_q_vote_state BIT(1);

    WHILE i < 10 DO
        SET v_member_idx = FLOOR(RAND() * 10) + 1; -- 1 이상 10 이하의 랜덤한 수
        SET v_question_id = FLOOR(RAND() * 10) + 1; -- 1 이상 10 이하의 랜덤한 수
        SET v_q_vote_state = RAND() > 0.5; -- 50% 확률로 0 또는 1

        INSERT INTO `question_votes` (`q_vote_state`, `member_idx`, `question_id`)
        VALUES (v_q_vote_state, v_member_idx, v_question_id);

        SET i = i + 1;
    END WHILE;
END //

CALL InsertExampleQuestionVotes();

SELECT * FROM question_votes;

SHOW CREATE TABLE question_votes;