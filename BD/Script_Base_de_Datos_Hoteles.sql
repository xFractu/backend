
/*CREACIÓN DE LA BASE DE DATOS*/
DROP DATABASE dbhotel;

CREATE DATABASE dbhotel;

CREATE USER hotel@'%' IDENTIFIED BY 'hotel';

GRANT ALL PRIVILEGES ON dbhotel.* TO hotel@'%';

/*USAR LA BASE DE DATOS*/
use dbhotel;

/*CREAR TABLA USUARIOS*/
drop table usuarios;
CREATE TABLE usuarios (
    id VARCHAR(255),
    correo VARCHAR(255),
    password VARCHAR(255),
    nombre VARCHAR(255),
    PRIMARY KEY (id)
);

/*CREAR TABLA HOTEL*/
drop table hotel;
CREATE TABLE hotel (
    id VARCHAR(255),
    nombre VARCHAR(255),
    estado VARCHAR(255),
    pais VARCHAR(255),
    precio VARCHAR(255),
    PRIMARY KEY (id)
);

/*CREAR TABLA RESERVACION*/
drop table reservacion;
CREATE TABLE reservacion (
    id_reservacion INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario VARCHAR(255),
    id_hotel VARCHAR(255),
    check_in VARCHAR(255),
    check_out VARCHAR(255),
    personas VARCHAR(255),
    
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id),
    FOREIGN KEY (id_hotel) REFERENCES hotel(id)
);

/*INSERTAR USUARIO*/
INSERT INTO usuarios values('ceb6729-07f3-483a-b6df-fd075f22327f','mcnd@gmail.com','1234','MCND');

/*INSERTAR HOTELES*/
INSERT INTO hotel (id, nombre, estado, pais, precio) 
VALUES ('1', 'Holiday Inn Express', 'Veracruz', 'Mexico', '1,600');

INSERT INTO hotel (id, nombre, estado, pais, precio) 
VALUES ('2', 'Hotel Flamingo Vallarta', 'Jalisco', 'Mexico', '2,300');

INSERT INTO hotel (id, nombre, estado, pais, precio) 
VALUES ('3', 'Hotel Esperanza', 'Baja California Sur', 'Mexico', '4,000');

INSERT INTO hotel (id, nombre, estado, pais, precio) 
VALUES ('4', 'Hotel Gamma Acapulco', 'Guerrero', 'Mexico', '2,500');

INSERT INTO hotel (id, nombre, estado, pais, precio) 
VALUES ('5', 'Hotel Royal Solaris', 'Quintana Roo', 'Mexico', '4,370');

INSERT INTO hotel (id, nombre, estado, pais, precio) 
VALUES ('6', 'Hotel GR Caribe', 'Quintana Roo', 'Mexico', '4,200');

/*INSERTAR RESERVACION*/
insert into reservacion values('1','ceb6729-07f3-483a-b6df-fd075f22327f','1','21/12/2023','26/12/2023','1');

/*CONSULTAS A LAS TABLAS*/
select * from usuarios;
select * from hotel;
select * from reservacion;
