CREATE TABLE favorite_topics (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    topic_id UUID NOT NULL,
    CONSTRAINT fk_topic
        FOREIGN KEY (topic_id)
        REFERENCES topics (id)
        ON DELETE CASCADE
);
