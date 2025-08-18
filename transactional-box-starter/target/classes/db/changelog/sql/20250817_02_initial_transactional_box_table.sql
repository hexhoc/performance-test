--liquibase formatted sql
--changeset hexhoc:20250817_02_initial_transactional_box_table.sql dbms=postgresql

CREATE TABLE tbox.incoming_events (
	created_at timestamp(6) NOT NULL,
	id uuid NOT NULL,
	request_id uuid NOT NULL,
	event_type varchar(50) NOT NULL,
	"source" varchar(50) NOT NULL,
	trace_id varchar(50) NOT NULL,
	status varchar(255) NULL,
	payload jsonb NULL,
	CONSTRAINT incoming_events_pkey PRIMARY KEY (id),
	CONSTRAINT incoming_events_status_check CHECK (((status)::text = ANY ((ARRAY['SUCCESS'::character varying, 'FAILED'::character varying])::text[])))
);

CREATE TABLE tbox.outgoing_events (
	created_at timestamp(6) NOT NULL,
	id uuid NOT NULL,
	incoming_event_id uuid NOT NULL,
	request_id uuid NOT NULL,
	destination varchar(50) NOT NULL,
	event_type varchar(50) NOT NULL,
	trace_id varchar(50) NOT NULL,
	payload jsonb NULL,
	CONSTRAINT outgoing_events_pkey PRIMARY KEY (id)
);