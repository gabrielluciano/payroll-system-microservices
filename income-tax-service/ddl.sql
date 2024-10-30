CREATE SEQUENCE IF NOT EXISTS sequence_tax_rates
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 50;


CREATE TABLE IF NOT EXISTS tax_rates (
    id BIGINT PRIMARY KEY DEFAULT nextval('sequence_tax_rates'),
    minimum_salary_threshold DECIMAL(10,2) NOT NULL,
    maximum_salary_threshold DECIMAL(10,2) NOT NULL,
    rate DECIMAL(4,2) NOT NULL,
    deduction DECIMAL(10,2) NOT NULL,
    UNIQUE(minimum_salary_threshold, maximum_salary_threshold)
);

