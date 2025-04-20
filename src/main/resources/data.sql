-- Insert roles
INSERT INTO roles (id, name) VALUES (1, 'ADMIN');

INSERT INTO roles (id, name) VALUES (2, 'USER');

-- Insert users (encrypted password with BCrypt: "password")
INSERT INTO users (username, password, role_id) VALUES
('admin', '$2a$10$yAGoDU3PImJkjHnra/aPBObwY4FitCde7FLtgIVqw/9HMN5Hv7nBy', 1);

INSERT INTO users (username, password, role_id) VALUES
('user',  '$2a$10$yAGoDU3PImJkjHnra/aPBObwY4FitCde7FLtgIVqw/9HMN5Hv7nBy', 2);