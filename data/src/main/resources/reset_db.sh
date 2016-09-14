# This script requires PGPASSWORD environment variable to be set to run
{
  "${PGPASSWORD?Need to set PGPASSWORD env var}"
} &> /dev/null

echo "DROP DATABASE pupddb; \q\n" | psql -U postgres

psql -U postgres -f ./sql/create_schema.sql
psql -U postgres -f ./sql/seed.sql