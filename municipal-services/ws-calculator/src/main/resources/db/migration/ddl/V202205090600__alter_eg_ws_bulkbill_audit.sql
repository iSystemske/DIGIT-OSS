ALTER TABLE eg_ws_bulkbill_audit 
ADD COLUMN IF NOT EXISTS audittime bigint NOT NULL,
ADD COLUMN IF NOT EXISTS message CHARACTER VARYING (2048) NOT NULL;
