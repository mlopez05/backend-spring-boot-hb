-- public.especialidad definition

-- Drop table

-- DROP TABLE especialidad;

CREATE TABLE especialidad (
	id serial4 NOT NULL,
	nombre varchar(50) NOT NULL,
	CONSTRAINT especialidad_pkey PRIMARY KEY (id)
);


-- public.hospital definition

-- Drop table

-- DROP TABLE hospital;

CREATE TABLE hospital (
	id serial4 NOT NULL,
	nombre varchar(50) NOT NULL,
	CONSTRAINT hospital_pkey PRIMARY KEY (id)
);


-- public.medico definition

-- Drop table

-- DROP TABLE medico;

CREATE TABLE medico (
	id_medico serial4 NOT NULL,
	nombres varchar(100) NOT NULL,
	apellidos varchar(100) NOT NULL,
	no_colegiado varchar(50) NOT NULL,
	id_especialidad int4 NOT NULL,
	email varchar(30) NOT NULL,
	telefono varchar(30) NULL,
	id_hospital int4 NOT NULL,
	estado public.estado_medico NULL DEFAULT 'ACTIVO'::estado_medico,
	id_usuario int4 NULL,
	CONSTRAINT medico_pkey PRIMARY KEY (id_medico),
	CONSTRAINT fk_medico_especialidad FOREIGN KEY (id_especialidad) REFERENCES especialidad(id),
	CONSTRAINT fk_medico_hospital FOREIGN KEY (id_hospital) REFERENCES hospital(id)
);


-- public.slot_agenda definition

-- Drop table

-- DROP TABLE slot_agenda;

CREATE TABLE slot_agenda (
	id_slot serial4 NOT NULL,
	id_medico int4 NOT NULL,
	fecha date NOT NULL,
	hora_inicio time NOT NULL,
	hora_fin time NOT NULL,
	estado varchar(20) NOT NULL DEFAULT 'DISPONIBLE'::character varying,
	CONSTRAINT chk_slot_estado CHECK (((estado)::text = ANY ((ARRAY['DISPONIBLE'::character varying, 'OCUPADO'::character varying])::text[]))),
	CONSTRAINT chk_slot_horas CHECK ((hora_fin > hora_inicio)),
	CONSTRAINT slot_agenda_pkey PRIMARY KEY (id_slot),
	CONSTRAINT fk_slot_medico FOREIGN KEY (id_medico) REFERENCES medico(id_medico) ON DELETE CASCADE
);