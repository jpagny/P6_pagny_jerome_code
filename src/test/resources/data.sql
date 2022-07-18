INSERT INTO `account` (`iban`, `balance`) VALUES ('aaa-aaa-aaa', '1000');
INSERT INTO `account` (`iban`, `balance`) VALUES ('aaa-bbb-aaa', '1500');
INSERT INTO `account` (`iban`, `balance`) VALUES ('aaa-ccc-aaa', '900');

INSERT INTO `user` (`id`, `email_address`, `first_name`, `last_name`, `password`, `account_id`) VALUES (1, 'pagny.jerome@gmail.com', 'jerome', 'pagny', '$2a$12$5St.kfMV0Aunu2U9bv/Ex..lA/Bwi.ML1NARoVeJBuinA/Nz1LINe', 'aaa-aaa-aaa');
INSERT INTO `user` (`id`, `email_address`, `first_name`, `last_name`, `password`, `account_id`) VALUES (2, 'pagny.nicolas@gmail.com', 'nicolas', 'pagny', '$2a$12$J75iv1kQwoCrV9jd6lapXeHNphvSAs9t1pq9md8ZnyOtJeCdeBSsG', 'aaa-bbb-aaa');
INSERT INTO `user` (`id`, `email_address`, `first_name`, `last_name`, `password`, `account_id`) VALUES (3, 'bouilly.pauline@gmail.com', 'pauline', 'bouilly', '$2a$12$ajXr8x.FFM7GLEXXTL9AB.ZCV/Bp8bY.al4oyj.at9S.DhRq7RscG', 'aaa-ccc-aaa');

INSERT INTO `user_friends` (`user_id`,`friends_id`) VALUES (1,3);