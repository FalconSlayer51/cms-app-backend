-- Program & Lesson status
CREATE TYPE program_status AS ENUM ('draft', 'published', 'archived');
CREATE TYPE lesson_status AS ENUM ('draft', 'scheduled', 'published', 'archived');

-- Lesson content
CREATE TYPE content_type AS ENUM ('video', 'article');

-- Assets
CREATE TYPE asset_variant AS ENUM ('portrait', 'landscape', 'square', 'banner');
CREATE TYPE asset_type AS ENUM ('poster', 'thumbnail', 'subtitle');

CREATE TABLE programs
(
    id                  UUID PRIMARY KEY,
    title               TEXT           NOT NULL,
    description         TEXT,
    language_primary    TEXT           NOT NULL,
    languages_available TEXT[]         NOT NULL,
    status              program_status NOT NULL DEFAULT 'draft',
    published_at        TIMESTAMPTZ,
    created_at          TIMESTAMPTZ    NOT NULL DEFAULT now(),
    updated_at          TIMESTAMPTZ    NOT NULL DEFAULT now(),

    CONSTRAINT program_lang_primary_check
        CHECK (language_primary = ANY (languages_available))
);

CREATE INDEX idx_program_status_lang_pub
    ON programs (status, language_primary, published_at);

CREATE TABLE topics
(
    id   UUID PRIMARY KEY,
    name TEXT NOT NULL UNIQUE
);

CREATE TABLE program_topics
(
    program_id UUID NOT NULL REFERENCES programs (id) ON DELETE CASCADE,
    topic_id   UUID NOT NULL REFERENCES topics (id) ON DELETE CASCADE,
    PRIMARY KEY (program_id, topic_id)
);

CREATE INDEX idx_program_topics_topic
    ON program_topics (topic_id);

CREATE TABLE terms
(
    id          UUID PRIMARY KEY,
    program_id  UUID        NOT NULL REFERENCES programs (id) ON DELETE CASCADE,
    term_number INT         NOT NULL,
    title       TEXT,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),

    UNIQUE (program_id, term_number)
);

CREATE TABLE lessons
(
    id                          UUID PRIMARY KEY,
    term_id                     UUID          NOT NULL REFERENCES terms (id) ON DELETE CASCADE,
    lesson_number               INT           NOT NULL,
    title                       TEXT          NOT NULL,
    content_type                content_type  NOT NULL,
    duration_ms                 BIGINT,
    is_paid                     BOOLEAN       NOT NULL DEFAULT FALSE,

    content_language_primary    TEXT          NOT NULL,
    content_languages_available TEXT[]        NOT NULL,
    content_urls_by_language    JSONB         NOT NULL,

    subtitle_languages          TEXT[],
    subtitle_urls_by_language   JSONB,

    status                      lesson_status NOT NULL DEFAULT 'draft',
    publish_at                  TIMESTAMPTZ,
    published_at                TIMESTAMPTZ,

    created_at                  TIMESTAMPTZ   NOT NULL DEFAULT now(),
    updated_at                  TIMESTAMPTZ   NOT NULL DEFAULT now(),

    UNIQUE (term_id, lesson_number),

    CONSTRAINT lesson_primary_lang_check
        CHECK (content_language_primary = ANY (content_languages_available)),

    CONSTRAINT scheduled_requires_publish_at
        CHECK (
            NOT (status = 'scheduled' AND publish_at IS NULL)
            ),

    CONSTRAINT published_requires_published_at
        CHECK (
            NOT (status = 'published' AND published_at IS NULL)
            )
);

CREATE INDEX idx_lesson_status_publish
    ON lessons (status, publish_at);

CREATE INDEX idx_lesson_term_number
    ON lessons (term_id, lesson_number);

CREATE TABLE program_assets
(
    id         UUID PRIMARY KEY,
    program_id UUID          NOT NULL REFERENCES programs (id) ON DELETE CASCADE,
    language   TEXT          NOT NULL,
    variant    asset_variant NOT NULL,
    asset_type asset_type    NOT NULL,
    url        TEXT          NOT NULL,

    UNIQUE (program_id, language, variant, asset_type)
);

CREATE INDEX idx_program_assets_lookup
    ON program_assets (program_id, language);

CREATE TABLE lesson_assets
(
    id         UUID PRIMARY KEY,
    lesson_id  UUID       NOT NULL REFERENCES lessons (id) ON DELETE CASCADE,
    language   TEXT       NOT NULL,
    variant    asset_variant,
    asset_type asset_type NOT NULL,
    url        TEXT       NOT NULL,

    UNIQUE (lesson_id, language, variant, asset_type)
);

CREATE INDEX idx_lesson_assets_lookup
    ON lesson_assets (lesson_id, language);

