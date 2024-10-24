DROP TABLE IF EXISTS product;

CREATE TABLE product (
                         id SERIAL PRIMARY KEY,
                         product_code VARCHAR(255),
                         product_category VARCHAR(255),
                         base_price INT,
                         current_price INT,
                         available_quantity INT
);

INSERT INTO product (product_code, product_category, base_price, current_price, available_quantity)
VALUES
    ('P001', 'ELECTRONICS', 100, 100, 50),
    ('P002', 'CLOTHING', 200, 200, 30),
    ('P003', 'FURNITURE', 150, 150, 20),
    ('P004', 'ACCESSORIES', 50, 50, 100),
    ('P005', 'ELECTRONICS', 300, 300, 10),
    ('P006', 'CLOTHING', 120, 120, 15),
    ('P007', 'FURNITURE', 250, 250, 40),
    ('P008', 'ACCESSORIES', 70, 70, 80),
    ('P009', 'ELECTRONICS', 400, 400, 5),
    ('P010', 'CLOTHING', 90, 90, 60),
    ('P011', 'FURNITURE', 110, 110, 35),
    ('P012', 'ACCESSORIES', 60, 60, 90),
    ('P013', 'ELECTRONICS', 220, 220, 25),
    ('P014', 'CLOTHING', 180, 180, 45),
    ('P015', 'FURNITURE', 320, 320, 18),
    ('P016', 'ACCESSORIES', 40, 40, 110),
    ('P017', 'ELECTRONICS', 500, 500, 8),
    ('P018', 'CLOTHING', 130, 130, 70),
    ('P019', 'FURNITURE', 270, 270, 12),
    ('P020', 'ACCESSORIES', 80, 80, 65);
