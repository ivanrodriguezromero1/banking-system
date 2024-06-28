CREATE TABLE IF NOT EXISTS customer (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    document_type VARCHAR(50) NOT NULL,
    document_number VARCHAR(50) NOT NULL,
    unique_code VARCHAR(100) NOT NULL
);
