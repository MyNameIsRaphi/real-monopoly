-- @block 
-- create bank table 
CREATE TABLE IF NOT EXISTS banks (
    id BIGINT PRIMARY KEY,
    reserve DOUBLE NOT NULL, 
    highest_credit_id BIGINT NOT NULL, 
    inflation DOUBLE NOT NULL
    FOREIGN KEY (game_id) REFERENCES games(id)
);

-- @block 
-- create games table
CREATE TABLE IF NOT EXISTS games (
    id BIGINT PRIMARY KEY, 
    number_of_players BIGINT, 
    FOREIGN KEY (user_id) REFERENCES users(id) 
);

-- @block 
-- create user table 
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY, 
    name VARCHAR(255) NOT NULL, 
    password VARCHAR(255) NOT NULL
);
