
-- public."comments" definition

-- Drop table

-- DROP TABLE public."comments";

CREATE TABLE public."comments" (
	created_at timestamp(6) NOT NULL,
	updated_at timestamp(6) NOT NULL,
	id uuid NOT NULL,
	post_entity_id uuid NULL,
	"content" varchar(255) NOT NULL,
	CONSTRAINT comments_pkey PRIMARY KEY (id),
	CONSTRAINT fkap6f3nc7mbdpa9on8mnv50e8t FOREIGN KEY (post_entity_id) REFERENCES public.posts(id)
);