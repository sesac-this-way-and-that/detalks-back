USE detalks;

DROP PROCEDURE InsertExampleMembers;

CREATE PROCEDURE InsertExampleMembers()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE member_email VARCHAR(255);
    DECLARE member_name VARCHAR(255);
    DECLARE member_img VARCHAR(255);
    DECLARE member_about VARCHAR(255);
    DECLARE member_summary VARCHAR(255);
    DECLARE member_rep INT;
    DECLARE member_social ENUM('NONE', 'GOOGLE');

    WHILE i <= 10 DO
        SET member_email = CONCAT('member', i, '@example.com');
        SET member_name = CONCAT('Member', i);
        SET member_img = CONCAT('default', (i % 5) + 1, '.png');
        SET member_about = CONCAT('About member ', i);
        SET member_summary = CONCAT('Summary member ', i);
        SET member_rep = FLOOR(1 + RAND() * 500);
        SET member_social = IF(RAND() < 0.5, 'NONE', 'GOOGLE');

        INSERT INTO `members` (
            `member_about`, `member_created`, `member_deleted`,
            `member_email`, `member_img`, `member_isdeleted`, `member_name`,
            `member_pwd`, `member_reason`, `member_rep`,
            `member_role`, `member_social`, `member_state`, `member_summary`,
            `member_updated`, `member_visited`
        ) VALUES (
            member_about, NOW(), NULL, member_email, member_img, b'0', 
            member_name, CONCAT('password', i), '', member_rep, 'USER', 
            member_social, b'1', member_summary, NULL, NOW()
        );

        SET i = i + 1;
    END WHILE;
END //

CALL InsertExampleMembers();

SELECT * FROM members;