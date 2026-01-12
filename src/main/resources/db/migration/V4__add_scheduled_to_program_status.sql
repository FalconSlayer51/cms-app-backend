-- Add new value to program_status enum
ALTER TYPE program_status
    ADD VALUE IF NOT EXISTS 'scheduled';
