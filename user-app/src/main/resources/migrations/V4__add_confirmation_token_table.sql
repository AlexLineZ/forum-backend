ALTER TABLE users
    ADD COLUMN is_enabled BOOLEAN NOT NULL DEFAULT FALSE;

CREATE TABLE confirmation_token (
       token_id BIGSERIAL PRIMARY KEY,
       confirmation_token UUID NOT NULL,
       created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
       user_id UUID NOT NULL,
       CONSTRAINT fk_user
           FOREIGN KEY(user_id)
               REFERENCES users(id)
               ON DELETE CASCADE
);

CREATE INDEX idx_confirmation_token_user_id ON confirmation_token(user_id);
ALTER TABLE confirmation_token ADD CONSTRAINT unique_confirmation_token UNIQUE (confirmation_token);
