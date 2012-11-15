# User schema

# --- !Ups

INSERT INTO `user`(id, name, password, bio, create_date, update_date, version, deleted)
    values('56d62ff0-92a7-4d51-8acf-0efd25d9ec9e', 'user1', 'test1', 'test1', now(), now(), 0, 0);

INSERT INTO `user`(id, name, password, bio, create_date, update_date, version, deleted)
    values('61157972-b49c-406a-a7d2-2762f95d8e0d', 'user2', 'test2', 'test2', now(), now(), 0, 0);

INSERT INTO `user_follow`(id, from_user_id, to_user_id, create_date, update_date, version, deleted)
    values('d748e439-3798-4aee-baa9-84fb6bd856ec', '56d62ff0-92a7-4d51-8acf-0efd25d9ec9e', '61157972-b49c-406a-a7d2-2762f95d8e0d', now(), now(), 0, 0);

INSERT INTO `user_status`(id, from_user_Id, status_type, status, create_date, update_date, version, deleted)
    values('226bcad1-db03-4397-90b6-34f9b2925cd0', '56d62ff0-92a7-4d51-8acf-0efd25d9ec9e', 1, 'つぶやき1', now(), now(), 0, 0);

INSERT INTO `user_status_to_user`(id, user_status_id, to_user_Id, create_date, update_date, version, deleted)
    values('49a532ca-a568-4035-a8e6-55aa5fd2f2fb', '226bcad1-db03-4397-90b6-34f9b2925cd0', '61157972-b49c-406a-a7d2-2762f95d8e0d', now(), now(), 0, 0);

# --- !Downs
DELETE FROM `user_status_to_user`;

DELETE FROM `user_status`;

DELETE FROM `user_follow`;

DELETE FROM `user`;




