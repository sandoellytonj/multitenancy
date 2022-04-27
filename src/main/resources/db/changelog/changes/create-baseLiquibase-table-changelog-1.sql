-- Table: public.baseLiquibase

-- DROP TABLE public.baseLiquibase;

CREATE TABLE public.baseLiquibase
(
    id bigint NOT NULL,
    nome character varying(255) COLLATE pg_catalog."default" NOT NULL,
    senha character varying(255) COLLATE pg_catalog."default" NOT NULL,
    url character varying(255) COLLATE pg_catalog."default" NOT NULL,
    usuario character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT baseLiquibase_pkey PRIMARY KEY (id)
)

    TABLESPACE pg_default;

ALTER TABLE public.baseLiquibase
    OWNER to root;