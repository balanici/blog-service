-- public.posts definition

-- Drop table

-- DROP TABLE public.posts;

CREATE TABLE public.posts (
	"views" int4 NULL,
	created_at timestamp(6) NOT NULL,
	updated_at timestamp(6) NOT NULL,
	id uuid NOT NULL,
	"content" varchar(1000) NOT NULL,
	post_status varchar(255) NOT NULL,
	title varchar(255) NOT NULL,
	CONSTRAINT posts_pkey PRIMARY KEY (id),
	CONSTRAINT posts_post_status_check CHECK (((post_status)::text = ANY ((ARRAY['DRAFT'::character varying, 'PUBLISHED'::character varying, 'ARCHIVED'::character varying, 'DELETED'::character varying])::text[])))
);
