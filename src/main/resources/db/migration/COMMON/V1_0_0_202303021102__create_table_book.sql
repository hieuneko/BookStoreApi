CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE books
(
    id          UUID         NOT NULL PRIMARY KEY DEFAULT uuid_generate_v4(),
    title       VARCHAR(255) NOT NULL,
    author      VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP,
    image       VARCHAR(255),
    user_id     UUID         NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

INSERT INTO books(title, author, description, user_id)
VALUES ('Harry Potter','JK. Rolling', 'Kha la hay', (SELECT id FROM users WHERE username = 'admin'))
