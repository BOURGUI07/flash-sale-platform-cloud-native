-- Drop tables if they exist
DROP TABLE IF EXISTS customer CASCADE;
DROP TABLE IF EXISTS order_history;

-- Create the customer table
CREATE TABLE customer (
                          id SERIAL PRIMARY KEY,  -- SERIAL is PostgreSQL's auto-increment equivalent
                          name VARCHAR(50),
                          balance INT,
                          shipping_address VARCHAR(200)
);

-- Create the order_history table
CREATE TABLE order_history (
                               id SERIAL PRIMARY KEY,  -- SERIAL for auto-increment
                               customer_id INT,
                               product_code VARCHAR(20),
                               product_category VARCHAR(20),
                               quantity INT,
                               price INT,
                               order_status VARCHAR(20),
                               order_date TIMESTAMP,
                               FOREIGN KEY (customer_id) REFERENCES customer(id)  -- Set up the foreign key relationship
);

-- Insert data into the customer table
INSERT INTO customer (name, balance, shipping_address)
VALUES
    ('Alice', 5000, '123 Main St'),
    ('Bob', 3000, '456 Oak Ave'),
    ('Charlie', 2000, '789 Pine Rd');
