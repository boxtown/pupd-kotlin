DROP TABLE IF EXISTS "public"."exercises";

CREATE TABLE "public"."exercises" (
    "id" UUID PRIMARY KEY,
    "name" NVARCHAR NOT NULL
);

INSERT INTO "public"."exercises" VALUES ('00000000-0000-4000-8000-000000000001', 'Test Exercise A');