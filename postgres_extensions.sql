 
/**
	Regardless we are using postgres db isolated way
	We are going to create our own schema in order to create our own USER table
	because postgres has already one
*/
CREATE SCHEMA crm;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";