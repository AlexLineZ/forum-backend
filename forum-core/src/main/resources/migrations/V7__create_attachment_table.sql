CREATE TABLE attachment (
     id UUID PRIMARY KEY,
     message_id UUID,
     name VARCHAR(255),
     size BIGINT,
     file_id UUID,
     FOREIGN KEY (message_id) REFERENCES messages(id) ON DELETE CASCADE
);
