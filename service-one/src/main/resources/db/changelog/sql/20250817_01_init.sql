--liquibase formatted sql
--changeset hexhoc:20250817_01_init.sql dbms=postgresql

CREATE TABLE public.operations (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    container_id BIGINT NOT NULL,
    amount NUMERIC(20, 2) NOT NULL,
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT false
);
