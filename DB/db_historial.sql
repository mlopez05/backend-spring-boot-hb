-- public.registros_clinicos definition

-- Drop table

-- DROP TABLE registros_clinicos;

CREATE TABLE registros_clinicos (
	id bigserial NOT NULL,
	paciente_id int8 NOT NULL,
	medico_id int4 NOT NULL,
	nombre_medico varchar(255) NOT NULL,
	especialidad varchar(150) NOT NULL,
	fecha_consulta timestamp NOT NULL,
	diagnostico_principal varchar(500) NOT NULL,
	receta_medica varchar(1000) NULL,
	observaciones varchar(2000) NULL,
	created_at timestamp NOT NULL DEFAULT now(),
	CONSTRAINT chk_diagnostico_no_vacio CHECK ((TRIM(BOTH FROM diagnostico_principal) <> ''::text)),
	CONSTRAINT registros_clinicos_pkey PRIMARY KEY (id)
);
CREATE INDEX idx_rc_medico ON public.registros_clinicos USING btree (medico_id);
CREATE INDEX idx_rc_paciente_fecha ON public.registros_clinicos USING btree (paciente_id, fecha_consulta DESC);