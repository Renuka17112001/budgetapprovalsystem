-- Create the database
CREATE DATABASE IF NOT EXISTS budget_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Use the database
USE budget_db;

-- Drop existing tables if needed (for clean setup)
DROP TABLE IF EXISTS audit_log;
DROP TABLE IF EXISTS budget_request;
DROP TABLE IF EXISTS department;

-- Table: department
CREATE TABLE department (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    current_budget DECIMAL(15,2) NOT NULL,
    yearly_allocation DECIMAL(15,2) NOT NULL,
    date_created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table: budget_request
CREATE TABLE budget_request (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    requested_amount DECIMAL(15,2) NOT NULL,
    purpose VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,
    requested_by VARCHAR(255) NOT NULL,
    approved_by VARCHAR(255),
    department_id BIGINT NOT NULL,
    date_created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_updated DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_budget_request_department FOREIGN KEY (department_id) REFERENCES department(id)
);

-- Table: audit_log
CREATE TABLE audit_log (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    action VARCHAR(50) NOT NULL,
    entity_id BIGINT NOT NULL,
    entity_type ENUM('BudgetRequest', 'Department') NOT NULL,
    old_value TEXT,
    new_value TEXT,
    changed_by VARCHAR(255),
    timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Insert a sample department
INSERT INTO department (name, current_budget, yearly_allocation, date_created, last_updated)
VALUES ('IT', 100000.00, 200000.00, NOW(), NOW());
