# GR07_1BT3_622_25A
Nuestro proyecto consiste en el desarrollo de una aplicación de trueque de ropa llamada TruequeApp.
Esta plataforma permite a los usuarios intercambiar prendas de vestir sin necesidad de utilizar dinero. Los participantes pueden publicar los artículos que ofrecen y aquellos que desean obtener, realizando un "match" con otros usuarios que coincidan en sus intereses.
TruequeApp integra funcionalidades como un sistema de mensajería (chat) y un sistema de reputación para valorar las transacciones realizadas.

Nota:
Actualmente nos encontramos en una fase temprana de implementación, por lo que todo el desarrollo se está realizando de manera local. La base de datos utilizada es MySQL.

Por favor, cree la base de datos llamada truequeapp_db e incluya las siguientes tablas:
CREATE TABLE closets (
    id BIGINT NOT NULL AUTO_INCREMENT,
    publicado BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id)
);

CREATE TABLE usuarios (
    cedula VARCHAR(10) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    fechaRegistro TIMESTAMP NOT NULL,
    closet_id BIGINT,
    PRIMARY KEY (cedula),
    FOREIGN KEY (closet_id) REFERENCES closets(id) ON DELETE SET NULL
);

CREATE TABLE prendas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    talla VARCHAR(50),
    estado VARCHAR(50),
    categoria VARCHAR(50),
    imagenURL VARCHAR(255),
    closet_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (closet_id) REFERENCES closets(id) ON DELETE CASCADE
);

Además, asegúrese de configurar el archivo persistence.xml incluyendo las credenciales (usuario y contraseña) correspondientes para la conexión a su instancia de MySQL.
