# --- !Ups

INSERT INTO CRM.USER (NAME, SURNAME, ROLE_ID) VALUES ('Admin', 'User', 1);;

INSERT INTO CRM.LOGIN (USERNAME, PASSWORD, PASSWORD_SALT, USER_ID) VALUES
('admin', '53e8d2ca0073a82adaa77112e3265a0c201ccb1328540e6971087b633c520a8e', '31a1ffde-e144-4f76-8226-b70a7130c76b', 1);;

COMMIT;;

# --- !Downs