INSERT IGNORE INTO user_role VALUES ('ADMIN'), ('SALE_ADMIN'), ('BUYER');

INSERT IGNORE INTO user_role_permissions VALUES('SALE_ADMIN','CREATE_ORDER');

INSERT IGNORE INTO `user` (`email`, `full_name`, `password`, `phone`, `status`, `user_name`, `role_name`) VALUES ('admin01@gmail.com', 'Admin 01', '$2a$12$Kd9zLjVySmSDfjtdQi9Yx.kJneINeBzHJWZTb4uuXxO4awDqvftSa', '0123456789', '0', 'admin01', 'ADMIN');
INSERT IGNORE INTO `user` (`email`, `full_name`, `password`, `phone`, `status`, `user_name`, `role_name`) VALUES ('admin02@gmail.com', 'Admin 02', '$2a$12$Kd9zLjVySmSDfjtdQi9Yx.kJneINeBzHJWZTb4uuXxO4awDqvftSa', '0123456788', '0', 'admin02', 'ADMIN');

-- INSERT IGNORE INTO user_role_permissions VALUES('ADMIN','ADMIN');

-- Buyer rank
INSERT IGNORE INTO buyer_rank VALUES (3, 0.1, 'Gold', 10000, null), (2, 0.05, 'Silver', 5000, 3), (1, 0.03, 'Bronze', 1000, 2);

-- Product category
INSERT IGNORE INTO product_category (`level`, `name`, `status`) VALUES ('0', 'Phụ kiện nam', '0');
INSERT IGNORE INTO product_category (`level`, `name`, `status`) VALUES ('0', 'Phụ kiện nữ', '0');
INSERT IGNORE INTO product_category (`level`, `name`, `status`) VALUES ('0', 'Phụ kiện khác', '0');