CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    registration_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_update_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    phone BIGINT UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    is_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    is_blocked BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT email_unique UNIQUE (email)
);

--Пароль password123
INSERT INTO users (id, first_name, last_name, email, phone, password, role, is_enabled, is_blocked)
VALUES
    ('b3b2d4b8-6232-4b1a-a5a1-0f8ce314d257', 'Sasha', 'Kirsanov', 'a1@example.com', 1234567890, '$2a$10$PC0tEoiGaVaWqcRP1osgj.27kZ7zGCLdUEhcr8w.1RLbhVgYCXVnO', 'ADMIN', TRUE, FALSE),
    ('f6fcb0f6-ee76-4fc4-89ae-330f5f5cc3f5', 'Yury', 'Sitdikov', 'a2@example.com', 2345678901, '$2a$10$PC0tEoiGaVaWqcRP1osgj.27kZ7zGCLdUEhcr8w.1RLbhVgYCXVnO', 'ADMIN', TRUE, FALSE),
    ('a80f0ad9-1620-4d4c-b1a7-48861d5a5dbc', 'Sasha', 'Bazdyrev', 'a3@example.com', 3456789012, '$2a$10$PC0tEoiGaVaWqcRP1osgj.27kZ7zGCLdUEhcr8w.1RLbhVgYCXVnO', 'MODERATOR', TRUE, FALSE),
    ('25d9a983-4a84-4145-af3b-6184969c2a13', 'Sergey', 'Lelikov', 'a4@example.com', 4567890123, '$2a$10$PC0tEoiGaVaWqcRP1osgj.27kZ7zGCLdUEhcr8w.1RLbhVgYCXVnO', 'USER', TRUE, FALSE),
    ('5b058a47-35e3-491c-bd55-1c609a0c4a0e', 'Marina', 'Ryzkova', 'a5@example.com', 5678901234, '$2a$10$PC0tEoiGaVaWqcRP1osgj.27kZ7zGCLdUEhcr8w.1RLbhVgYCXVnO', 'USER', TRUE, FALSE);