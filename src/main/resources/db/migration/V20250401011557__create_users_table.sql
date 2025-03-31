-- public.users definition

-- Drop table

-- DROP TABLE public.users;

CREATE TABLE public.users (
     id SERIAL NOT NULL,
     "username" VARCHAR(45) NOT NULL,
     "password" VARCHAR(45) NOT NULL,
     "enabled" int2 NOT NULL,
     CONSTRAINT users_pkey PRIMARY KEY (id)
);
