-- public.rol definition

-- Drop table

-- DROP TABLE rol;

CREATE TABLE rol (
	id serial4 NOT NULL,
	nombre varchar(30) NOT NULL,
	descripcion varchar(200) NULL,
	CONSTRAINT rol_pkey PRIMARY KEY (id)
);


-- public.usuario definition

-- Drop table

-- DROP TABLE usuario;

CREATE TABLE usuario (
	id serial4 NOT NULL,
	usuario varchar(30) NOT NULL,
	nombre varchar(50) NOT NULL,
	apellido varchar(50) NOT NULL,
	contraseña varchar NOT NULL,
	rol_id int4 NOT NULL,
	CONSTRAINT usuario_pkey PRIMARY KEY (id),
	CONSTRAINT usuario_rol_id_fkey FOREIGN KEY (rol_id) REFERENCES rol(id)
);