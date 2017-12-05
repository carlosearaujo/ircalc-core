--
-- PostgreSQL database dump
--

-- Dumped from database version 10.1
-- Dumped by pg_dump version 10.1

-- Started on 2017-12-05 12:55:54

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 8 (class 2615 OID 17401)
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
-- TOC entry 2841 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = ircalc, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 198 (class 1259 OID 17407)
-- Name: finalized_trade; Type: TABLE; Schema: ircalc; Owner: postgres
--

CREATE TABLE finalized_trade (
    id bigint NOT NULL,
    close_time integer,
    deal_fee_aliquot double precision NOT NULL,
    exchange_fee_aliquot double precision NOT NULL,
    registration_fee_aliquot double precision NOT NULL,
    close_trade_id bigint
);


ALTER TABLE finalized_trade OWNER TO postgres;

--
-- TOC entry 199 (class 1259 OID 17412)
-- Name: finalized_trade_trade_rel; Type: TABLE; Schema: ircalc; Owner: postgres
--

CREATE TABLE finalized_trade_trade_rel (
    finalized_trade_id bigint NOT NULL,
    referenced_trades_id bigint NOT NULL
);


ALTER TABLE finalized_trade_trade_rel OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 17430)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: ircalc; Owner: postgres
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE hibernate_sequence OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 17415)
-- Name: open_trade; Type: TABLE; Schema: ircalc; Owner: postgres
--

CREATE TABLE open_trade (
    id bigint NOT NULL,
    close_time integer,
    market_direction integer,
    open_quantity bigint,
    ticket character varying(255)
);


ALTER TABLE open_trade OWNER TO postgres;

--
-- TOC entry 201 (class 1259 OID 17420)
-- Name: open_trade_trade_rel; Type: TABLE; Schema: ircalc; Owner: postgres
--

CREATE TABLE open_trade_trade_rel (
    open_trade_id bigint NOT NULL,
    reference_trades_id bigint NOT NULL
);


ALTER TABLE open_trade_trade_rel OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 17402)
-- Name: trade; Type: TABLE; Schema: ircalc; Owner: postgres
--

CREATE TABLE trade (
    id bigint NOT NULL,
    broker_tax double precision NOT NULL,
    broker_tax_fee double precision NOT NULL,
    date timestamp without time zone NOT NULL,
    market_direction integer NOT NULL,
    market_type integer NOT NULL,
    price_per_unit double precision NOT NULL,
    quantity bigint NOT NULL,
    ticket character varying(255) NOT NULL
);


ALTER TABLE trade OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 17423)
-- Name: virtual_trade; Type: TABLE; Schema: ircalc; Owner: postgres
--

CREATE TABLE virtual_trade (
    id bigint NOT NULL,
    quantity bigint,
    trade_id bigint
);


ALTER TABLE virtual_trade OWNER TO postgres;

--
-- TOC entry 2829 (class 0 OID 17407)
-- Dependencies: 198
-- Data for Name: finalized_trade; Type: TABLE DATA; Schema: ircalc; Owner: postgres
--

COPY finalized_trade (id, close_time, deal_fee_aliquot, exchange_fee_aliquot, registration_fee_aliquot, close_trade_id) FROM stdin;
\.


--
-- TOC entry 2830 (class 0 OID 17412)
-- Dependencies: 199
-- Data for Name: finalized_trade_trade_rel; Type: TABLE DATA; Schema: ircalc; Owner: postgres
--

COPY finalized_trade_trade_rel (finalized_trade_id, referenced_trades_id) FROM stdin;
\.


--
-- TOC entry 2831 (class 0 OID 17415)
-- Dependencies: 200
-- Data for Name: open_trade; Type: TABLE DATA; Schema: ircalc; Owner: postgres
--

COPY open_trade (id, close_time, market_direction, open_quantity, ticket) FROM stdin;
\.


--
-- TOC entry 2832 (class 0 OID 17420)
-- Dependencies: 201
-- Data for Name: open_trade_trade_rel; Type: TABLE DATA; Schema: ircalc; Owner: postgres
--

COPY open_trade_trade_rel (open_trade_id, reference_trades_id) FROM stdin;
\.


--
-- TOC entry 2828 (class 0 OID 17402)
-- Dependencies: 197
-- Data for Name: trade; Type: TABLE DATA; Schema: ircalc; Owner: postgres
--

COPY trade (id, broker_tax, broker_tax_fee, date, market_direction, market_type, price_per_unit, quantity, ticket) FROM stdin;
1	0	0.050000000000000003	2014-02-21 00:00:00	0	0	12.85	100	CMIG4
2	0	0.050000000000000003	2014-02-24 00:00:00	1	1	0.34000000000000002	100	CMIGC18
3	0	0.050000000000000003	2014-03-07 00:00:00	0	1	0.10000000000000001	100	CMIGC18
4	0	0.050000000000000003	2014-03-07 00:00:00	0	0	27.77	98	VALE5
5	0	0.050000000000000003	2014-03-13 00:00:00	0	0	26.100000000000001	2	VALE5
6	0	0.050000000000000003	2014-03-13 00:00:00	1	0	13.470000000000001	100	CMIG4
7	0	0.050000000000000003	2014-03-14 00:00:00	0	0	13.1	99	CMIG4
8	0	0.050000000000000003	2014-03-18 00:00:00	1	0	13.279999999999999	99	CMIG4
9	0	0.050000000000000003	2014-03-20 00:00:00	1	1	0.35999999999999999	100	VALED28
10	0	0.050000000000000003	2014-04-14 00:00:00	0	1	0	100	VALED28
11	6	0.050000000000000003	2014-04-14 00:00:00	1	0	28	100	VALE5
12	0	0.050000000000000003	2014-04-17 00:00:00	0	0	28.25	100	VALE5
13	0	0.050000000000000003	2014-04-17 00:00:00	1	1	0.56000000000000005	100	VALEE30
14	0	0.050000000000000003	2014-04-28 00:00:00	0	0	26.699999999999999	11	VALE5
15	1.5	0.050000000000000003	2014-05-08 00:00:00	0	0	27.199999999999999	51	VALE5
16	1.5	0.050000000000000003	2014-06-06 00:00:00	0	0	26.120000000000001	48	VALE5
17	1.5	0.050000000000000003	2014-06-09 00:00:00	1	1	0.32000000000000001	200	VALEG29
18	1.5	0.050000000000000003	2014-07-18 00:00:00	0	1	0.23999999999999999	200	VALEG29
19	1.5	0.050000000000000003	2014-07-18 00:00:00	1	1	0.48999999999999999	200	VALEH29
20	1.5	0.050000000000000003	2014-08-04 00:00:00	0	0	13.199999999999999	72	GRND3
21	1.5	0.050000000000000003	2014-08-08 00:00:00	0	1	0.14999999999999999	200	VALEH29
22	1.5	0.050000000000000003	2014-08-08 00:00:00	1	0	28.129999999999999	3	VALE5
23	1.5	0.050000000000000003	2014-08-15 00:00:00	1	0	14.140000000000001	72	GRND3
24	1.5	0.050000000000000003	2014-08-18 00:00:00	1	1	0.60999999999999999	200	VALEJ60
25	1.5	0.050000000000000003	2014-08-18 00:00:00	0	0	15.720000000000001	75	GETI3
26	1.5	0.050000000000000003	2014-08-26 00:00:00	1	0	16.66	75	GETI3
27	1.5	0.050000000000000003	2014-08-27 00:00:00	0	0	33.189999999999998	37	PSSA3
28	1.5	0.050000000000000003	2014-08-28 00:00:00	0	1	0.20000000000000001	200	VALEJ60
29	1.5	0.050000000000000003	2014-08-28 00:00:00	1	0	33.369999999999997	37	PSSA3
30	1.5	0.050000000000000003	2014-08-28 00:00:00	0	0	26.399999999999999	46	VALE5
31	1.5	0.050000000000000003	2014-09-23 00:00:00	0	0	24.039999999999999	100	VALE5
32	1.5	0.050000000000000003	2014-10-02 00:00:00	0	0	19.93	100	EZTC3
33	1.5	0.050000000000000003	2014-10-02 00:00:00	1	0	24.350000000000001	100	VALE5
34	1.5	0.050000000000000003	2014-10-03 00:00:00	0	0	29.399999999999999	86	PSSA3
35	1.5	0.050000000000000003	2014-10-03 00:00:00	1	0	20.329999999999998	100	EZTC3
36	1.5	0.050000000000000003	2014-10-14 00:00:00	1	1	0.23999999999999999	200	VALEK28
37	1.5	0.050000000000000003	2014-10-17 00:00:00	1	0	29.600000000000001	86	PSSA3
38	1.5	0.050000000000000003	2014-10-17 00:00:00	0	0	23.050000000000001	47	VALE5
39	1.5	0.050000000000000003	2014-10-17 00:00:00	0	0	14.52	100	GETI3
40	1.5	0.050000000000000003	2014-10-29 23:00:00	0	0	20.510000000000002	5	VALE5
41	1.5	0.050000000000000003	2014-11-02 23:00:00	1	0	21.18	2	VALE5
42	1.5	0.050000000000000003	2014-11-03 23:00:00	0	0	20.899999999999999	69	VALE5
43	1.5	0.050000000000000003	2014-11-06 23:00:00	0	0	20.75	28	VALE5
44	1.5	0.050000000000000003	2014-11-06 23:00:00	1	0	14.9	100	GETI3
45	1.5	0.050000000000000003	2014-11-09 23:00:00	0	0	20.309999999999999	44	VALE5
46	1.5	0.050000000000000003	2014-11-18 23:00:00	1	1	0.20000000000000001	400	VALEA65
47	1.5	0.050000000000000003	2014-12-04 23:00:00	0	0	18.629999999999999	100	VALE5
48	1.5	0.050000000000000003	2014-12-08 23:00:00	0	1	0.050000000000000003	400	VALEA65
49	1.5	0.050000000000000003	2014-12-17 23:00:00	1	1	0.23999999999999999	500	VALEB49
50	1.5	0.050000000000000003	2015-01-22 23:00:00	0	1	0.14999999999999999	500	VALEB49
51	1.5	0.050000000000000003	2015-02-02 23:00:00	1	1	0.28999999999999998	500	VALEC49
52	1.5	0.050000000000000003	2015-03-02 00:00:00	0	1	0.089999999999999997	500	VALEC49
53	1.5	0.050000000000000003	2015-04-23 00:00:00	1	1	0.28000000000000003	500	VALEE19
54	1.5	0.050000000000000003	2015-04-30 00:00:00	0	1	0.17999999999999999	500	VALEE19
55	1.5	0.050000000000000003	2015-04-30 00:00:00	1	1	0.23999999999999999	500	VALEE20
56	1.5	0.050000000000000003	2015-05-08 00:00:00	0	0	18.75	28	VALE5
57	1.5	0.050000000000000003	2015-05-11 00:00:00	0	0	13.699999999999999	1	HGTX3
58	1.5	0.050000000000000003	2015-05-11 00:00:00	1	0	19.280000000000001	28	VALE5
59	1.5	0.050000000000000003	2015-05-12 00:00:00	0	0	13.699999999999999	47	HGTX3
60	1.5	0.050000000000000003	2015-05-12 00:00:00	0	0	13.9	38	HGTX3
61	1.5	0.050000000000000003	2015-05-12 00:00:00	0	1	0.11	500	VALEE20
62	1.5	0.050000000000000003	2015-06-01 00:00:00	1	1	0.53000000000000003	500	VALEG19
63	1.5	0.050000000000000003	2015-06-05 00:00:00	0	0	13.57	14	HGTX3
64	1.5	0.050000000000000003	2015-06-16 00:00:00	0	1	0.37	500	VALEG19
65	1.5	0.050000000000000003	2015-06-16 00:00:00	0	0	15.359999999999999	50	EZTC3
66	1.5	0.050000000000000003	2015-07-06 00:00:00	1	0	14.199999999999999	50	EZTC3
67	1.5	0.050000000000000003	2015-07-06 00:00:00	0	0	14.880000000000001	29	VALE5
68	1.5	0.050000000000000003	2015-07-06 00:00:00	1	0	12.039999999999999	100	HGTX3
69	1.5	0.050000000000000003	2015-07-06 00:00:00	0	0	14.880000000000001	100	VALE5
70	1.5	0.050000000000000003	2015-07-30 00:00:00	0	0	14.630000000000001	3	VALE5
71	1.5	0.050000000000000003	2015-07-30 00:00:00	0	1	0.34999999999999998	600	VALEI18
72	1.5	0.050000000000000003	2015-07-30 00:00:00	1	1	0.44	600	VALEI18
\.


--
-- TOC entry 2833 (class 0 OID 17423)
-- Dependencies: 202
-- Data for Name: virtual_trade; Type: TABLE DATA; Schema: ircalc; Owner: postgres
--

COPY virtual_trade (id, quantity, trade_id) FROM stdin;
\.


--
-- TOC entry 2842 (class 0 OID 0)
-- Dependencies: 203
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: ircalc; Owner: postgres
--

SELECT pg_catalog.setval('hibernate_sequence', 72, true);


--
-- TOC entry 2694 (class 2606 OID 17411)
-- Name: finalized_trade finalized_trade_pkey; Type: CONSTRAINT; Schema: ircalc; Owner: postgres
--

ALTER TABLE ONLY finalized_trade
    ADD CONSTRAINT finalized_trade_pkey PRIMARY KEY (id);


--
-- TOC entry 2696 (class 2606 OID 17419)
-- Name: open_trade open_trade_pkey; Type: CONSTRAINT; Schema: ircalc; Owner: postgres
--

ALTER TABLE ONLY open_trade
    ADD CONSTRAINT open_trade_pkey PRIMARY KEY (id);


--
-- TOC entry 2692 (class 2606 OID 17406)
-- Name: trade trade_pkey; Type: CONSTRAINT; Schema: ircalc; Owner: postgres
--

ALTER TABLE ONLY trade
    ADD CONSTRAINT trade_pkey PRIMARY KEY (id);


--
-- TOC entry 2698 (class 2606 OID 17429)
-- Name: open_trade_trade_rel uk_be16th0ctl79jgooowhus2vo0; Type: CONSTRAINT; Schema: ircalc; Owner: postgres
--

ALTER TABLE ONLY open_trade_trade_rel
    ADD CONSTRAINT uk_be16th0ctl79jgooowhus2vo0 UNIQUE (reference_trades_id);


--
-- TOC entry 2700 (class 2606 OID 17427)
-- Name: virtual_trade virtual_trade_pkey; Type: CONSTRAINT; Schema: ircalc; Owner: postgres
--

ALTER TABLE ONLY virtual_trade
    ADD CONSTRAINT virtual_trade_pkey PRIMARY KEY (id);


--
-- TOC entry 2702 (class 2606 OID 17437)
-- Name: finalized_trade_trade_rel fka0jbbo3bt7vqoshta6lric83; Type: FK CONSTRAINT; Schema: ircalc; Owner: postgres
--

ALTER TABLE ONLY finalized_trade_trade_rel
    ADD CONSTRAINT fka0jbbo3bt7vqoshta6lric83 FOREIGN KEY (referenced_trades_id) REFERENCES virtual_trade(id);


--
-- TOC entry 2703 (class 2606 OID 17442)
-- Name: finalized_trade_trade_rel fkbbr6gd3duy7aupjp99sq8hqum; Type: FK CONSTRAINT; Schema: ircalc; Owner: postgres
--

ALTER TABLE ONLY finalized_trade_trade_rel
    ADD CONSTRAINT fkbbr6gd3duy7aupjp99sq8hqum FOREIGN KEY (finalized_trade_id) REFERENCES finalized_trade(id);


--
-- TOC entry 2705 (class 2606 OID 17452)
-- Name: open_trade_trade_rel fkgsns2t9wdqyb25fpd47u8xbr4; Type: FK CONSTRAINT; Schema: ircalc; Owner: postgres
--

ALTER TABLE ONLY open_trade_trade_rel
    ADD CONSTRAINT fkgsns2t9wdqyb25fpd47u8xbr4 FOREIGN KEY (open_trade_id) REFERENCES open_trade(id);


--
-- TOC entry 2706 (class 2606 OID 17457)
-- Name: virtual_trade fki8qiqc8pca27h0ilts9cbegin; Type: FK CONSTRAINT; Schema: ircalc; Owner: postgres
--

ALTER TABLE ONLY virtual_trade
    ADD CONSTRAINT fki8qiqc8pca27h0ilts9cbegin FOREIGN KEY (trade_id) REFERENCES trade(id);


--
-- TOC entry 2701 (class 2606 OID 17432)
-- Name: finalized_trade fkn4ghhdt74534p83weyrmcllii; Type: FK CONSTRAINT; Schema: ircalc; Owner: postgres
--

ALTER TABLE ONLY finalized_trade
    ADD CONSTRAINT fkn4ghhdt74534p83weyrmcllii FOREIGN KEY (close_trade_id) REFERENCES virtual_trade(id);


--
-- TOC entry 2704 (class 2606 OID 17447)
-- Name: open_trade_trade_rel fknxnlv439yqg7abus9otncd5t6; Type: FK CONSTRAINT; Schema: ircalc; Owner: postgres
--

ALTER TABLE ONLY open_trade_trade_rel
    ADD CONSTRAINT fknxnlv439yqg7abus9otncd5t6 FOREIGN KEY (reference_trades_id) REFERENCES virtual_trade(id);


-- Completed on 2017-12-05 12:55:55

--
-- PostgreSQL database dump complete
--

