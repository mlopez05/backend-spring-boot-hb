-- public.notificacion definition

-- Drop table

-- DROP TABLE notificacion;

CREATE TABLE notificacion (
	id serial4 NOT NULL,
	id_usuario int4 NOT NULL,
	titulo varchar(200) NULL,
	mensaje text NULL,
	leido bool NULL DEFAULT false,
	fecha date NULL,
	CONSTRAINT notificacion_pkey PRIMARY KEY (id)
);