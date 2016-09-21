-- this script assumes create_schema.sql has been properly run first

-- connect to correct database
\connect pupddb;

\set id1 '\'00000000-0000-4000-8000-000000000001\''
\set id2 '\'00000000-0000-4000-8000-000000000002\''
\set id3 '\'00000000-0000-4000-8000-000000000003\''
\set id4 '\'00000000-0000-4000-8000-000000000004\''
\set id5 '\'00000000-0000-4000-8000-000000000005\''
\set id6 '\'00000000-0000-4000-8000-000000000006\''
\set id7 '\'00000000-0000-4000-8000-000000000007\''
\set id8 '\'00000000-0000-4000-8000-000000000008\''

-- insert some basic exercises
INSERT INTO public.exercises VALUES (:id1, 'Back Squat');
INSERT INTO public.exercises VALUES (:id2, 'Front Squat');
INSERT INTO public.exercises VALUES (:id3, 'Flat Bench Press');
INSERT INTO public.exercises VALUES (:id4, 'Incline Bench Press');
INSERT INTO public.exercises VALUES (:id5, 'Overhead Press');
INSERT INTO public.exercises VALUES (:id6, 'Power Clean');
INSERT INTO public.exercises VALUES (:id7, 'Deadlift');
INSERT INTO public.exercises VALUES (:id8, 'Chin-ups');

-- seed with SS to start
INSERT INTO public.workouts VALUES (:id1, 'Starting Strength Phase 1 Day A');
INSERT INTO public.workouts VALUES (:id2, 'Starting Strength Phase 1 Day B');
INSERT INTO public.workouts VALUES (:id3, 'Starting Strength Phase 2 Day A');
INSERT INTO public.workouts VALUES (:id4, 'Starting Strength Phase 2 Day B');
INSERT INTO public.workouts VALUES (:id5, 'Starting Strength Phase 3 Day A');
INSERT INTO public.workouts VALUES (:id6, 'Starting Strength Phase 3 Day B');

-- SS P1 DA
INSERT INTO public.workout_exercises VALUES (
  :id1,
  :id1,
  :id1,
  '{5,5,5}',
  '{100.0,100.0,100.0}',
  5.0
);
INSERT INTO public.workout_exercises VALUES (
  :id2,
  :id1,
  :id3,
  '{5,5,5}',
  '{100.0,100.0,100.0}',
  5.0
);
INSERT INTO public.workout_exercises VALUES (
  :id3,
  :id1,
  :id7,
  '{5}',
  '{100.0}',
  10.0
);

-- SS P1 DB
INSERT INTO public.workout_exercises VALUES (
  :id4,
  :id2,
  :id1,
  '{5,5,5}',
  '{100.0,100.0,100.0}',
  5.0
);
INSERT INTO public.workout_exercises VALUES (
  :id5,
  :id2,
  :id5,
  '{5,5,5}',
  '{100.0,100.0,100.0}',
  5.0
);
INSERT INTO public.workout_exercises VALUES (
  :id6,
  :id2,
  :id5,
  '{5,5,5}',
  '{100.0,100.0,100.0}',
  5.0
);

-- SS P1 Program
INSERT INTO public.programs VALUES (:id1, 'Starting Strength Phase 1');
INSERT INTO public.program_workouts VALUES (
  :id1,
  :id1,
  :id1,
  0
);
INSERT INTO public.program_workouts VALUES (
  :id2,
  :id1,
  :id2,
  1
)