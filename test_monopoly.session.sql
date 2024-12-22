-- @block
-- create player table

CREATE TABLE IF NOT EXISTS players (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255)  NOT NULL,
    asset_wealth DOUBLE NOT NULL,
    liquid_wealth DOUBLE NOT NULL
);


-- @block
-- create credits table
CREATE TABLE IF NOT EXISTS credits (
    id BIGINT PRIMARY KEY, 
    FOREIGN KEY (player_id) REFERENCES  players(id),
    loan DOUBLE NOT NULL, 
    interest_rate DOUBLE 
);

-- @block 
-- create properties table 
CREATE TABLE IF NOT EXISTS properties (
    id BIGINT PRIMARY KEY, 
    name VARCHAR(255) NOT NULL, 
    category VARCHAR(255) NOT NULL, 
    value DOUBLE NOT NULL,
    FOREIGN KEY (player_id) REFERENCES players(id)M,
    price_bought BIGINT,
);