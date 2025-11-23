CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS accounts (
                                        id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    account_number VARCHAR(20) NOT NULL UNIQUE,
    customer_name VARCHAR(100) NOT NULL,
    active_account BOOLEAN NOT NULL,
    balance DECIMAL(18,2) NOT NULL DEFAULT 0,
    daily_transferred DECIMAL(18,2) NOT NULL DEFAULT 0,
    daily_reset DATE NOT NULL DEFAULT CURRENT_DATE
    );

CREATE INDEX IF NOT EXISTS idx_accounts_number ON accounts(account_number);

INSERT INTO accounts(id, account_number, customer_name, active_account, balance, daily_transferred)
VALUES
    ('db5ab4c8-6a6b-4bd5-a20c-919793e128d3','0001','João',TRUE,20000.00,0.00),
    ('9d965035-bba5-4e5e-bccd-28d19e69c86e','0002','Maria',TRUE,5000.00,0.00),
    ('914f6284-ee9d-47ad-ab0e-6b4a3a39a554','0003','Carlos',TRUE,5000.00,0.00)
    ON CONFLICT (account_number) DO NOTHING;
