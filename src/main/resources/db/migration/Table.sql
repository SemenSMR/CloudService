CREATE TABLE files (
                       id SERIAL PRIMARY KEY,
                       file_name VARCHAR(255) NOT NULL,
                       file_data BYTEA NOT NULL
);