# User schema

# --- !Ups

INSERT INTO `user`(id, name, password, create_date, update_date, version, deleted)
    values('user1', 'user1', 'test1', now(), now(), 0, 0);

INSERT INTO `user`(id, name, password, create_date, update_date, version, deleted)
    values('user2', 'user2', 'test2', now(), now(), 0, 0);

INSERT INTO `user_follow`(id, from_user_id, to_user_id, create_date, update_date, version, deleted)
    values('uf1', 'user1', 'user2', now(), now(), 0, 0);

INSERT INTO `user_status`(id, from_user_Id, status_type, status, create_date, update_date, version, deleted)
    values('us1', 'user1', 1, 'つぶやき1', now(), now(), 0, 0);

INSERT INTO `user_status_to_user`(id, user_status_id, to_user_Id, create_date, update_date, version, deleted)
    values('ustu1', 'us1', 'user2', now(), now(), 0, 0);

# --- !Downs
DELETE FROM `user_status_to_user`;

DELETE FROM `user_status`;

DELETE FROM `user_follow`;

DELETE FROM `user`;




