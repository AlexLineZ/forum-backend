CREATE TABLE notifications (
      id UUID PRIMARY KEY,
      user_id UUID NOT NULL,
      label VARCHAR(100) NOT NULL,
      message VARCHAR(1024) NOT NULL,
      date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
      read BOOLEAN NOT NULL DEFAULT FALSE,
      display_in_history BOOLEAN NOT NULL DEFAULT TRUE
);
