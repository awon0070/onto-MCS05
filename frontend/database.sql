CREATE TABLE users (
    first_name VARCHAR2(50 CHAR) NOT NULL,
    last_name VARCHAR2(50 CHAR) NOT NULL,
    doctor_id VARCHAR2(7 CHAR) NOT NULL,
    password_hash VARCHAR2(255 CHAR) NOT NULL, -- Store password hashes securely
    specialization VARCHAR2(100 CHAR) NOT NULL,

    -- Check that specialization is one of 'heart' or 'brain'
    CONSTRAINT chk_specialization CHECK (specialization IN ('heart', 'brain')),

    -- Check that doctor_id is in the format "AAA0000"
    CONSTRAINT chk_doctor_id_format CHECK (REGEXP_LIKE(doctor_id  '^[A-Z]{3}\d{4}$'))
);

-- Add an index on doctor_id for faster lookups
CREATE INDEX idx_doctor_id ON users (doctor_id);