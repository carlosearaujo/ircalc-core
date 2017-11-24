--
-- PostgreSQL database dump
--

-- Dumped from database version 10.1
-- Dumped by pg_dump version 10.1

-- Started on 2017-11-24 18:59:36

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 5 (class 2615 OID 16394)
-- Name: ircalc; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA ircalc;


ALTER SCHEMA ircalc OWNER TO postgres;

--
-- TOC entry 1 (class 3079 OID 12924)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2806 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = ircalc, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 199 (class 1259 OID 16404)
-- Name: TradeType; Type: TABLE; Schema: ircalc; Owner: postgres
--

CREATE TABLE "TradeType" (
    id integer NOT NULL,
    type character varying(10) NOT NULL
);


ALTER TABLE "TradeType" OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 16402)
-- Name: sqc_trade; Type: SEQUENCE; Schema: ircalc; Owner: postgres
--

CREATE SEQUENCE sqc_trade
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE sqc_trade OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 16395)
-- Name: trade; Type: TABLE; Schema: ircalc; Owner: postgres
--

CREATE TABLE trade (
    id integer NOT NULL,
    date date NOT NULL,
    ticket character varying(50) NOT NULL,
    quantity integer NOT NULL,
    price double precision NOT NULL
);


ALTER TABLE trade OWNER TO postgres;

--
-- TOC entry 2678 (class 2606 OID 16408)
-- Name: TradeType TradeType_pkey; Type: CONSTRAINT; Schema: ircalc; Owner: postgres
--

ALTER TABLE ONLY "TradeType"
    ADD CONSTRAINT "TradeType_pkey" PRIMARY KEY (id, type);


--
-- TOC entry 2676 (class 2606 OID 16399)
-- Name: trade trade_pkey; Type: CONSTRAINT; Schema: ircalc; Owner: postgres
--

ALTER TABLE ONLY trade
    ADD CONSTRAINT trade_pkey PRIMARY KEY (id);


-- Completed on 2017-11-24 18:59:36

--
-- PostgreSQL database dump complete
--

