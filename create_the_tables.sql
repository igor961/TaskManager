--
-- PostgreSQL database dump
--

-- Dumped from database version 11.8
-- Dumped by pg_dump version 11.8

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: projects; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.projects (
    id integer NOT NULL,
    name character varying(256)
);


--
-- Name: projects_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.projects ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.projects_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: tasks; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tasks (
    id integer NOT NULL,
    name character varying(256),
    status boolean,
    project_id integer NOT NULL
);


--
-- Name: tasks_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

ALTER TABLE public.tasks ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.tasks_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: projects projects_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.projects
    ADD CONSTRAINT projects_pkey PRIMARY KEY (id);


--
-- Name: tasks tasks_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT tasks_pkey PRIMARY KEY (id);


--
-- Name: tasks fk_project_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tasks
    ADD CONSTRAINT fk_project_id FOREIGN KEY (project_id) REFERENCES public.projects(id) MATCH FULL ON UPDATE CASCADE ON DELETE CASCADE;


ALTER TABLE public.tasks
   ADD COLUMN priority integer DEFAULT 1;


CREATE OR REPLACE FUNCTION public.get_aux_id_for_tasks(
	priority integer,
	cur_id integer,
	max_id integer)
    RETURNS text
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE
AS $BODY$
declare
   compl integer;
begin
   SELECT floor(pow(10, log(max_id)-log(cur_id)))
   INTO compl
   FROM tasks;

   return concat(priority, compl, cur_id);
end;
$BODY$;

ALTER TABLE public.tasks
    ADD COLUMN term timestamp without time zone;

--
-- PostgreSQL database dump complete
--

