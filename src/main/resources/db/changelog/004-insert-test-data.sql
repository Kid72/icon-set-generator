--liquibase formatted sql

--changeset iconset:006-insert-test-data context:test
--comment: Insert 100,000 test icons for E2E testing and development

INSERT INTO icons (name, category, tags, metadata)
SELECT
    'icon_' || i,
    CASE (i % 10)
        WHEN 0 THEN 'nature'
        WHEN 1 THEN 'technology'
        WHEN 2 THEN 'food'
        WHEN 3 THEN 'travel'
        WHEN 4 THEN 'business'
        WHEN 5 THEN 'health'
        WHEN 6 THEN 'education'
        WHEN 7 THEN 'entertainment'
        WHEN 8 THEN 'sports'
        ELSE 'other'
    END,
    ARRAY['tag_' || (i % 50)::TEXT, 'tag_' || ((i+1) % 50)::TEXT, 'tag_' || ((i+2) % 50)::TEXT],
    jsonb_build_object(
        'test', true,
        'index', i,
        'batch', i / 1000,
        'priority', (i % 5) + 1
    )
FROM generate_series(1, 100000) AS i;

-- Update statistics for accurate query planning
ANALYZE icons;

--rollback DELETE FROM icons WHERE (metadata->>'test')::boolean = true;
