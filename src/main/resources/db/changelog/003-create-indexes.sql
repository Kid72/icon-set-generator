--liquibase formatted sql

--changeset iconset:005-create-indexes
--comment: Create performance indexes for icons table queries

-- Index on category for filtering
CREATE INDEX IF NOT EXISTS idx_icons_category
ON icons USING BTREE(category);

-- GIN index on tags array for fast tag lookups
CREATE INDEX IF NOT EXISTS idx_icons_tags
ON icons USING GIN(tags);

-- BRIN index on created_at for time-based queries (efficient for large tables)
CREATE INDEX IF NOT EXISTS idx_icons_created
ON icons USING BRIN(created_at) WITH (pages_per_range = 128);

-- Update statistics for query planner
ANALYZE icons;
ANALYZE icon_sets;

--rollback DROP INDEX IF EXISTS idx_icons_category;
--rollback DROP INDEX IF EXISTS idx_icons_tags;
--rollback DROP INDEX IF EXISTS idx_icons_created;
