--liquibase formatted sql
--changeset hexhoc:20250817_01_init.sql dbms=postgresql

CREATE TABLE public.containers (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    amount NUMERIC(20, 2) NOT NULL,
    locked BOOLEAN NOT NULL DEFAULT false,
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE public.containers_history (
    id UUID PRIMARY KEY,
    operation_id UUID NOT NULL,
    operation_type VARCHAR(50) NOT NULL,
    amount NUMERIC(20, 2) NOT NULL,
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT false
);