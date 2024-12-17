-- @block
-- create player table

CREATE TABLE IF NOT EXISTS player (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255)  NOT NULL,
    asset_wealth DOUBLE,
    liquid_wealth DOUBLE
)
