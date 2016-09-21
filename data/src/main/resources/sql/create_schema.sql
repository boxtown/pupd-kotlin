-- create role for api access

CREATE ROLE api;

-- create pupd database

CREATE DATABASE pupddb OWNER postgres;

\connect pupddb;

-- create public schema

CREATE SCHEMA IF NOT EXISTS public;

-- create exercises table

CREATE TABLE IF NOT EXISTS public.exercises (
    id    UUID PRIMARY KEY,
    name  TEXT UNIQUE NOT NULL
);
GRANT SELECT, INSERT, UPDATE, DELETE ON public.exercises TO api;

-- create workouts table

CREATE TABLE IF NOT EXISTS public.workouts (
    id    UUID PRIMARY KEY,
    name  TEXT UNIQUE NOT NULL
);
GRANT SELECT, INSERT, UPDATE, DELETE on public.workouts TO api;

-- create mapping table for workouts <-> exercises

CREATE TABLE IF NOT EXISTS public.workout_exercises (
    id          UUID PRIMARY KEY,
    workout_id  UUID NOT NULL,
    exercise_id UUID NOT NULL,
    reps        integer[] NOT NULL,
    weights     double precision[] NOT NULL,
    incr        double precision NOT NULL,
    FOREIGN KEY (workout_id) REFERENCES public.workouts (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (exercise_id) REFERENCES public.exercises (id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
GRANT SELECT, INSERT, UPDATE, DELETE ON public.workout_exercises TO api;

-- create programs table

CREATE TABLE IF NOT EXISTS public.programs (
    id    UUID PRIMARY KEY,
    name  TEXT UNIQUE NOT NULL
);
GRANT SELECT, INSERT, UPDATE, DELETE on public.programs TO api;

-- create mapping table for programs <-> workouts

CREATE TABLE IF NOT EXISTS public.program_workouts (
    program_id      UUID NOT NULL,
    workout_id      UUID NOT NULL,
    pos             integer NOT NULL CHECK (pos >= 0),
    PRIMARY KEY (program_id, workout_id, pos),
    FOREIGN KEY (program_id) REFERENCES public.programs (id)
            ON UPDATE CASCADE
            ON DELETE CASCADE
);
GRANT SELECT, INSERT, UPDATE, DELETE on public.program_workouts TO api;