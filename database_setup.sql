-- ============================================================
-- Bank Management System - MySQL Database Setup Script
-- Run this script in MySQL Workbench or MySQL CLI
-- ============================================================

-- Step 1: Create the database
CREATE DATABASE IF NOT EXISTS bank_management_system;
USE bank_management_system;

-- ============================================================
-- TABLE 1: Account (Customer Accounts)
-- ============================================================
CREATE TABLE IF NOT EXISTS account (
    account_id   INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    email        VARCHAR(100) NOT NULL UNIQUE,
    phone        VARCHAR(20)  NOT NULL,
    balance      DOUBLE       NOT NULL DEFAULT 0.0,
    password     VARCHAR(100) NOT NULL
);

-- ============================================================
-- TABLE 2: Transaction (Deposits and Withdrawals)
-- ============================================================
CREATE TABLE IF NOT EXISTS transaction (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    account_id     INT          NOT NULL,
    type           VARCHAR(20)  NOT NULL,   -- 'DEPOSIT' or 'WITHDRAW'
    amount         DOUBLE       NOT NULL,
    date           DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transaction_account
        FOREIGN KEY (account_id) REFERENCES account(account_id)
        ON DELETE CASCADE
);

-- ============================================================
-- TABLE 3: Loan (Loan Applications)
-- ============================================================
CREATE TABLE IF NOT EXISTS loan (
    loan_id    INT AUTO_INCREMENT PRIMARY KEY,
    account_id INT          NOT NULL,
    amount     DOUBLE       NOT NULL,
    status     VARCHAR(20)  NOT NULL DEFAULT 'PENDING',  -- PENDING, APPROVED, REJECTED
    CONSTRAINT fk_loan_account
        FOREIGN KEY (account_id) REFERENCES account(account_id)
        ON DELETE CASCADE
);

-- ============================================================
-- TABLE 4: Admin (Administrator Credentials)
-- ============================================================
CREATE TABLE IF NOT EXISTS admin (
    admin_id  INT AUTO_INCREMENT PRIMARY KEY,
    username  VARCHAR(50)  NOT NULL UNIQUE,
    password  VARCHAR(100) NOT NULL
);

-- ============================================================
-- Insert Default Admin Account
-- Username: admin | Password: admin123
-- ============================================================
INSERT INTO admin (username, password) VALUES ('admin', 'admin123')
ON DUPLICATE KEY UPDATE username = username;

-- ============================================================
-- Sample customer data for testing (optional)
-- ============================================================
INSERT INTO account (name, email, phone, balance, password) VALUES
('Ali Hassan',    'ali@example.com',    '03001234567', 50000.00, 'pass123'),
('Sara Khan',     'sara@example.com',   '03111234567', 25000.00, 'pass456'),
('Ahmed Raza',    'ahmed@example.com',  '03211234567', 75000.00, 'pass789')
ON DUPLICATE KEY UPDATE name = name;

-- Verify tables created
SHOW TABLES;
SELECT 'Database setup complete!' AS Status;
