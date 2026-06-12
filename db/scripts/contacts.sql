CREATE TABLE IF NOT EXISTS users (
    id       BIGSERIAL     PRIMARY KEY,
    name     VARCHAR(100)  NOT NULL,
    surname  VARCHAR(100)  NOT NULL,
    email    VARCHAR(255)  NOT NULL UNIQUE,
    password VARCHAR(1000)  NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS contacts (
    id       BIGSERIAL     PRIMARY KEY,
    user_id  BIGINT        NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name     VARCHAR(100)  NOT NULL,
    surname  VARCHAR(100)  NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_contacts_user_id ON contacts(user_id);