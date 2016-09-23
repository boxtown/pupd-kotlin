DROP TABLE IF EXISTS "public"."workout_exercises";
DROP TABLE IF EXISTS "public"."workouts";

CREATE TABLE "public"."workouts" (
    "id" UUID PRIMARY KEY,
    "name" NVARCHAR NOT NULL
);

CREATE TABLE "public"."workout_exercises" (
    "id"          UUID PRIMARY KEY,
    "workout_id"  UUID NOT NULL,
    "exercise_id" UUID NOT NULL,
    "sets"        NVARCHAR NOT NULL,
    "incr"        DOUBLE NOT NULL,
    FOREIGN KEY ("workout_id") REFERENCES "public"."workouts" ("id")
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY ("exercise_id") REFERENCES "public"."exercises" ("id")
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

INSERT INTO "public"."workouts" VALUES ('00000000-0000-4000-8000-000000000001', 'Test Workout A');
INSERT INTO "public"."workouts" VALUES ('00000000-0000-4000-8000-000000000002', 'Test Workout C');
INSERT INTO "public"."workouts" VALUES ('00000000-0000-4000-8000-000000000003', 'Test Workout B');

INSERT INTO "public"."workout_exercises" VALUES (
    '00000000-0000-4000-8000-000000000001',
    '00000000-0000-4000-8000-000000000001',
    '00000000-0000-4000-8000-000000000001',
    '[{"reps":5, "weight": 85.0},{"reps":5, "weight": 95.0},{"reps":5, "weight": 100.0}]',
    5.0
);
INSERT INTO "public"."workout_exercises" VALUES (
    '00000000-0000-4000-8000-000000000002',
    '00000000-0000-4000-8000-000000000001',
    '00000000-0000-4000-8000-000000000002',
    '[{"reps":5, "weight": 65.0}]',
    10.0
);
INSERT INTO "public"."workout_exercises" VALUES (
    '00000000-0000-4000-8000-000000000003',
    '00000000-0000-4000-8000-000000000003',
    '00000000-0000-4000-8000-000000000003',
    '[{"reps":5, "weight": 100.0},{"reps":5, "weight": 100.0},{"reps":5, "weight": 100.0}]',
    3.0
);