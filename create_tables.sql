BEGIN;


CREATE TABLE IF NOT EXISTS public.field
(
    id integer NOT NULL DEFAULT nextval('field_id_seq'::regclass),
    name character varying(255) COLLATE pg_catalog."default",
    selector character varying(255) COLLATE pg_catalog."default",
    weight double precision NOT NULL,
    CONSTRAINT field_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.index
(
    id integer NOT NULL DEFAULT nextval('index_id_seq'::regclass),
    rank double precision NOT NULL,
    lemma_id integer NOT NULL,
    page_id integer NOT NULL,
    CONSTRAINT index_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.lemma
(
    id integer NOT NULL DEFAULT nextval('lemma_id_seq'::regclass),
    frequency integer NOT NULL,
    lemma character varying(255) COLLATE pg_catalog."default" NOT NULL,
    site_id integer NOT NULL,
    CONSTRAINT lemma_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.page
(
    id integer NOT NULL DEFAULT nextval('page_id_seq'::regclass),
    code integer NOT NULL,
    content text COLLATE pg_catalog."default" NOT NULL,
    path character varying(500) COLLATE pg_catalog."default" NOT NULL,
    site_id integer NOT NULL,
    CONSTRAINT page_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.site
(
    id integer NOT NULL DEFAULT nextval('site_id_seq'::regclass),
    last_error text COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    status character varying(255) COLLATE pg_catalog."default" NOT NULL,
    status_time timestamp without time zone NOT NULL,
    url character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT site_pkey PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS public.index
    ADD CONSTRAINT fk_index_lemma_id FOREIGN KEY (lemma_id)
    REFERENCES public.lemma (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;
CREATE INDEX IF NOT EXISTS ind_index_lemma_id
    ON public.index(lemma_id);


ALTER TABLE IF EXISTS public.index
    ADD CONSTRAINT fk_index_page_id FOREIGN KEY (page_id)
    REFERENCES public.page (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;
CREATE INDEX IF NOT EXISTS idx_index_page_id
    ON public.index(page_id);


ALTER TABLE IF EXISTS public.lemma
    ADD CONSTRAINT fk_lemma_site_id FOREIGN KEY (site_id)
    REFERENCES public.site (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;
CREATE INDEX IF NOT EXISTS idx_lemma_site_id
    ON public.lemma(site_id);


ALTER TABLE IF EXISTS public.page
    ADD CONSTRAINT fk_page_site_id FOREIGN KEY (site_id)
    REFERENCES public.site (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE;
CREATE INDEX IF NOT EXISTS idx_page_site_id
    ON public.page(site_id);

END;

INSERT INTO public.field(name, selector, weight) VALUES ('title', 'title', 1);
INSERT INTO public.field(name, selector, weight) VALUES ('body', 'body', 0.8);