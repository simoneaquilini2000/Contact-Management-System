CREATE TABLE IF NOT EXISTS contacts (
    id       BIGSERIAL     PRIMARY KEY,
    user_id  VARCHAR(36)   NOT NULL,
    name     VARCHAR(100)  NOT NULL,
    surname  VARCHAR(100)  NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_contacts_user_id ON contacts(user_id);