USE detalks;

DROP PROCEDURE InsertExampleAnswerVotes;

CREATE PROCEDURE InsertExampleAnswerVotes()
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE v_answer_id BIGINT;
    DECLARE v_member_idx BIGINT;
    DECLARE v_a_vote_state BIT(1);

    WHILE i < 10 DO
        SET v_answer_id = FLOOR(RAND() * 10) + 1; -- 1 이상 10 이하의 랜덤한 수
        SET v_member_idx = FLOOR(RAND() * 10) + 1; -- 1 이상 10 이하의 랜덤한 수
        SET v_a_vote_state = RAND() > 0.5; -- 50% 확률로 0 또는 1

        INSERT INTO `answer_votes` (`a_vote_state`, `answer_id`, `member_idx`)
        VALUES (v_a_vote_state, v_answer_id, v_member_idx);

        SET i = i + 1;
    END WHILE;
END //

CALL InsertExampleAnswerVotes();

SELECT * FROM answer_votes;

SHOW CREATE TABLE answer_votes;