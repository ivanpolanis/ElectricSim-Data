-- init.sql
CREATE USER grafana_reader WITH PASSWORD 'un_password_seguro';
GRANT CONNECT ON DATABASE electricsim TO grafana_reader;
GRANT USAGE ON SCHEMA public TO grafana_reader;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO grafana_reader;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT ON TABLES TO grafana_reader;
