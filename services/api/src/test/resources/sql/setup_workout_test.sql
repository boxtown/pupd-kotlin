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
    "reps"        ARRAY NOT NULL,
    "weights"     ARRAY NOT NULL,
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