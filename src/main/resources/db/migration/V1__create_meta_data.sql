CREATE TABLE file_meta_data (
     id UUID PRIMARY KEY,
     name VARCHAR(255) NOT NULL,
     size BIGINT NOT NULL,
     upload_time TIMESTAMP WITHOUT TIME ZONE NOT NULL
);