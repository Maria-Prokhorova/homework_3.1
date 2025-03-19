-- liquibase formatted sql

-- changeset mprokhorova:1
CREATE INDEX name_student_index ON student (name);

-- changeset mprokhorova:2
CREATE INDEX name_color_faculty_index ON faculty (name, color);