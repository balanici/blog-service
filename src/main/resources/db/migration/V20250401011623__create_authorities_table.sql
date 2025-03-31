-- public.authorities definition

-- Drop table

-- DROP TABLE public.authorities;

CREATE TABLE public.authorities (
  id SERIAL NOT NULL,
  username VARCHAR(45) NOT NULL,
  authority VARCHAR(45) NOT NULL,
  PRIMARY KEY (id)
);
