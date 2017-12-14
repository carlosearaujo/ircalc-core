SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_with_oids = false;

CREATE TABLE finalized_trade (
    id bigint NOT NULL,
    close_time integer,
    deal_fee_aliquot double precision NOT NULL,
    exchange_fee_aliquot double precision NOT NULL,
    registration_fee_aliquot double precision NOT NULL,
    close_trade_id bigint
);


ALTER TABLE finalized_trade OWNER TO postgres;

CREATE TABLE finalized_trade_trade_rel (
    finalized_trade_id bigint NOT NULL,
    virtual_trade_id bigint NOT NULL
);


ALTER TABLE finalized_trade_trade_rel OWNER TO postgres;

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE hibernate_sequence OWNER TO postgres;

CREATE TABLE open_trade (
    id bigint NOT NULL,
    close_time integer,
    market_direction integer,
    open_quantity bigint,
    ticket character varying(255)
);


ALTER TABLE open_trade OWNER TO postgres;

CREATE TABLE open_trade_trade_rel (
    open_trade_id bigint NOT NULL,
    virtual_trade_id bigint NOT NULL
);


ALTER TABLE open_trade_trade_rel OWNER TO postgres;

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

CREATE TABLE virtual_trade (
    id bigint NOT NULL,
    quantity bigint,
    trade_id bigint
);


ALTER TABLE virtual_trade OWNER TO postgres;

SELECT pg_catalog.setval('hibernate_sequence', 1, false);

ALTER TABLE ONLY finalized_trade
    ADD CONSTRAINT finalized_trade_pkey PRIMARY KEY (id);

ALTER TABLE ONLY open_trade
    ADD CONSTRAINT open_trade_pkey PRIMARY KEY (id);

ALTER TABLE ONLY trade
    ADD CONSTRAINT trade_pkey PRIMARY KEY (id);

ALTER TABLE ONLY open_trade_trade_rel
    ADD CONSTRAINT uk_b6c7sxy4p1sg2du0kgybbcd4c UNIQUE (virtual_trade_id);

ALTER TABLE ONLY virtual_trade
    ADD CONSTRAINT virtual_trade_pkey PRIMARY KEY (id);

ALTER TABLE ONLY finalized_trade_trade_rel
    ADD CONSTRAINT fk_finalized_trade_trade_rel_finalized_trade FOREIGN KEY (finalized_trade_id) REFERENCES finalized_trade(id) ON DELETE CASCADE;

ALTER TABLE ONLY finalized_trade_trade_rel
    ADD CONSTRAINT fk_finalized_trade_trade_rel_virtual_trade FOREIGN KEY (virtual_trade_id) REFERENCES virtual_trade(id) ON DELETE CASCADE;

ALTER TABLE ONLY open_trade_trade_rel
    ADD CONSTRAINT fk_open_trade_trade_rel_open_trade FOREIGN KEY (open_trade_id) REFERENCES open_trade(id);

ALTER TABLE ONLY open_trade_trade_rel
    ADD CONSTRAINT fk_open_trade_trade_rel_virtual_trade FOREIGN KEY (virtual_trade_id) REFERENCES virtual_trade(id) ON DELETE CASCADE;

ALTER TABLE ONLY virtual_trade
    ADD CONSTRAINT fki8qiqc8pca27h0ilts9cbegin FOREIGN KEY (trade_id) REFERENCES trade(id) ON DELETE CASCADE;

ALTER TABLE ONLY finalized_trade
    ADD CONSTRAINT fkn4ghhdt74534p83weyrmcllii FOREIGN KEY (close_trade_id) REFERENCES virtual_trade(id) ON DELETE CASCADE;