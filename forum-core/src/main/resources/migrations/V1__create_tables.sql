CREATE TABLE categories (
       id UUID PRIMARY KEY,
       created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
       modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
       created_by VARCHAR(255) NOT NULL,
       name VARCHAR(255) NOT NULL,
       parent_category_id UUID,
       FOREIGN KEY (parent_category_id) REFERENCES categories(id)
);

CREATE TABLE topics (
       id UUID PRIMARY KEY,
       created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
       modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
       created_by VARCHAR(255) NOT NULL,
       name VARCHAR(255) NOT NULL,
       category_id UUID NOT NULL,
       FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE messages (
       id UUID PRIMARY KEY,
       created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
       modified_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
       created_by VARCHAR(255) NOT NULL,
       text TEXT NOT NULL,
       topic_id UUID NOT NULL,
       FOREIGN KEY (topic_id) REFERENCES topics(id)
);
