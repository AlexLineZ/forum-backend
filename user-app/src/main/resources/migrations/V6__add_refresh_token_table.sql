CREATE TABLE refresh_tokens (
      id UUID PRIMARY KEY,
      token TEXT NOT NULL,
      expiration_date_time TIMESTAMP WITH TIME ZONE NOT NULL,
      user_id UUID NOT NULL,
      CONSTRAINT bk_user
          FOREIGN KEY(user_id)
              REFERENCES users(id)
              ON DELETE CASCADE
);