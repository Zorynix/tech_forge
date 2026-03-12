ALTER TABLE auth_service.users ADD COLUMN email VARCHAR(255);
CREATE UNIQUE INDEX idx_users_email ON auth_service.users (email) WHERE email IS NOT NULL;
