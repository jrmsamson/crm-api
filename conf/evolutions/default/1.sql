# --- !Ups

CREATE OR REPLACE FUNCTION generate_uuid()
  RETURNS trigger AS $generate_uuid_body$
BEGIN
  IF NEW.uuid IS NULL THEN
    SELECT uuid_generate_v4() INTO NEW.uuid;;
  END IF;;
  RETURN NEW;;
END;;
$generate_uuid_body$ LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION update_modified_timestamp()
  RETURNS trigger AS $update_modified_timestamp_body$
BEGIN
  IF NEW.modified IS NULL THEN
    NEW.modified = now();;
  END IF;;
  RETURN NEW;;
END;;
$update_modified_timestamp_body$ LANGUAGE plpgsql;;

CREATE OR REPLACE FUNCTION update_created_timestamp()
  RETURNS trigger AS $update_created_timestamp_body$
BEGIN
  IF NEW.created IS NULL THEN
    NEW.created = now();;
  END IF;;
  RETURN NEW;;
END;;
$update_created_timestamp_body$ LANGUAGE plpgsql;;

CREATE TABLE CRM.ROLE
(
  ID              SERIAL            PRIMARY KEY,
  NAME            VARCHAR(50)       NOT NULL
);;

INSERT INTO CRM.ROLE (NAME) VALUES ('Admin');;
INSERT INTO CRM.ROLE (NAME) VALUES ('User');;

CREATE TABLE CRM.USER
(
  ID                    BIGSERIAL     PRIMARY KEY,
  UUID                  UUID,
  NAME                  VARCHAR(50)   NOT NULL,
  SURNAME               VARCHAR(50)   NOT NULL,
  ACTIVE                BOOLEAN       NOT NULL    DEFAULT TRUE,
  ROLE_ID               SERIAL        NOT NULL,
  TOKEN                 VARCHAR(50),
  TOKEN_EXPIRATION      TIMESTAMP,
  CREATED               TIMESTAMP,
  MODIFIED              TIMESTAMP
);;

CREATE UNIQUE INDEX USER_NAME_SURNAME_UINDEX ON CRM.USER (NAME, SURNAME);;
CREATE UNIQUE INDEX USER_UUID_UINDEX ON CRM.USER (UUID);;

CREATE TRIGGER USER_UUID_TRIGGER
  BEFORE INSERT
  ON CRM.USER
  FOR EACH ROW
EXECUTE PROCEDURE generate_uuid();;

CREATE TRIGGER USER_CREATED_TRIGGER
  BEFORE INSERT
  ON CRM.USER
  FOR EACH ROW
EXECUTE PROCEDURE update_created_timestamp();;

CREATE TRIGGER USER_MODIFIED_TRIGGER
  BEFORE INSERT OR UPDATE
  ON CRM.USER
  FOR EACH ROW
EXECUTE PROCEDURE update_modified_timestamp();;

# --- !Downs

DROP TABLE CRM.ROLE;;
DROP TABLE CRM.USER;;
DROP FUNCTION generate_uuid();;
DROP FUNCTION update_modified_timestamp();;
DROP FUNCTION update_created_timestamp();;