INSERT INTO tb_authority(ID, AUTHORITY, AUTHORITY_NAME) values(1, 'ADMIN', '관리자');
INSERT INTO tb_authority(ID, AUTHORITY, AUTHORITY_NAME) values(2, 'MEMBER', '회원');

insert into tb_user (email, hp, login_fail_count, name, password, password_update_date, refresh_token, refresh_token_reg_date, reg_date, reg_user_id, salt, state, tel, update_date, user_id, id) values (NULL, NULL, NULL, '관리자', '$2a$10$EIDB/Y5nzEfFGIqSMjTW7./A9EzDNPHyV0Y8/tE.UouYZmPgXPni.', NULL, NULL, NULL, '08/15/2019 19:54:38.289', NULL, NULL, NULL, NULL, NULL, 'admin', 68);

insert into tb_user_authority (authority_id, user_id) values (1, 68)