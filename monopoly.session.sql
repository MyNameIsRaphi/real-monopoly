-- @block 
-- create bank table 
CREATE TABLE IF NOT EXISTS banks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    reserve DOUBLE NOT NULL, 
    highest_credit_id BIGINT NOT NULL, 
    inflation DOUBLE NOT NULL,
    game_id BIGINT NOT NULL,
    FOREIGN KEY (game_id) REFERENCES games(id)
);

-- @block 
-- create games table
CREATE TABLE IF NOT EXISTS games (
    id BIGINT PRIMARY KEY AUTO_INCREMENT, 
    number_of_players BIGINT, 
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) 
);

-- @block 
-- create user table 
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT, 
    name VARCHAR(255) NOT NULL UNIQUE, 
    password VARCHAR(255) NOT NULL
);
