ALTER TABLE users ADD COLUMN credentials_version bigint NOT NULL DEFAULT 0;
ALTER TABLE users ADD CONSTRAINT users_credentials_version_check CHECK (credentials_version >= 0);

CREATE TABLE password_reset_tokens (
    user_id integer PRIMARY KEY REFERENCES users (id) ON DELETE CASCADE,
    token_hash varchar(64) NOT NULL UNIQUE,
    requested_at timestamptz NOT NULL,
    expires_at timestamptz NOT NULL,
    CONSTRAINT password_reset_expiry_check CHECK (expires_at > requested_at)
);
