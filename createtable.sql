CREATE TYPE status AS ENUM('active','done', 'deleted');

-- Creation of task table
CREATE TABLE IF NOT EXISTS tasks (
  id  SERIAL PRIMARY KEY,   
  name varchar(250) NOT NULL,
  status status NOT NULL,
  creation_datetime TIMESTAMP NOT NULL DEFAULT now()
);