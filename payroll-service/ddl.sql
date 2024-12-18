CREATE SEQUENCE IF NOT EXISTS sequence_payrolls
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 50;


CREATE TABLE IF NOT EXISTS payrolls (
    id BIGINT PRIMARY KEY DEFAULT nextval('sequence_payrolls'),
    employee_cpf VARCHAR(14) NOT NULL,
    year INTEGER NOT NULL,
    month INTEGER NOT NULL,
    gross_pay DECIMAL(10,2) NOT NULL,
    net_pay DECIMAL(10,2) NOT NULL,
    UNIQUE(employee_cpf, year, month)
);
