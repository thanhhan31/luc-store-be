INSERT INTO user_role VALUES ('ADMIN'), ('SALE_ADMIN'), ('BUYER');
INSERT INTO user_role_permissions VALUES('ADMIN','ADMIN');

-- Buyer rank
INSERT INTO buyer_rank VALUES (3, 0.1, 'Gold', 10000, null), (2, 0.05, 'Silver', 5000, 3), (1, 0.03, 'Bronze', 1000, 2);