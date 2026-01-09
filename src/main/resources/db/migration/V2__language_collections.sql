CREATE TABLE program_languages
(
    program_id UUID NOT NULL
        REFERENCES programs (id)
            ON DELETE CASCADE,

    language   TEXT NOT NULL,

    PRIMARY KEY (program_id, language)
);
CREATE TABLE lesson_content_languages
(
    lesson_id UUID NOT NULL
        REFERENCES lessons (id)
            ON DELETE CASCADE,

    language  TEXT NOT NULL,

    PRIMARY KEY (lesson_id, language)
);
CREATE TABLE lesson_subtitle_languages
(
    lesson_id UUID NOT NULL
        REFERENCES lessons (id)
            ON DELETE CASCADE,

    language  TEXT NOT NULL,

    PRIMARY KEY (lesson_id, language)
);
ALTER TABLE programs
    DROP COLUMN IF EXISTS languages_available;
ALTER TABLE lessons
    DROP COLUMN IF EXISTS content_languages_available;

ALTER TABLE lessons
    DROP COLUMN IF EXISTS subtitle_languages;
