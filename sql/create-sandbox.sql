SET client_encoding = 'UNICODE';
SET check_function_bodies = false;
SET client_min_messages = warning;

CREATE DATABASE "sandbox"
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default;

ALTER DATABASE "sandbox" OWNER TO postgres;
