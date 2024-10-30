CREATE SEQUENCE IF NOT EXISTS sequence_positions
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 50;

CREATE TABLE IF NOT EXISTS positions (
    id BIGINT PRIMARY KEY DEFAULT nextval('sequence_positions'),
    name VARCHAR(50) NOT NULL,
    UNIQUE(name)
);

CREATE TABLE IF NOT EXISTS employees (
    cpf VARCHAR(14) PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    base_salary DECIMAL(10,2) NOT NULL,
    position_id BIGINT,
    CONSTRAINT fk_position FOREIGN KEY (position_id) REFERENCES positions(id)
);

