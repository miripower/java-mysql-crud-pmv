
# Java MySQL CRUD Application

Este proyecto es una aplicación de consola en Java que implementa un sistema CRUD (Crear, Leer, Actualizar, Eliminar) para gestionar categorías y productos en una base de datos MySQL. La aplicación utiliza **HikariCP** para la gestión de conexiones y **Maven** como herramienta de construcción.

## Requisitos

- **Java 17** o superior
- **Maven 3.6.1** o superior
- **Docker** y **Docker Compose**
- **MySQL 8**

## Estructura del Proyecto

```
├── docker/                        # Archivos de configuración de Docker
│   ├── docker-compose.yml         # Configuración de MySQL y PhpMyAdmin
├── java/                          # Código fuente de la aplicación
│   ├── lab-pmv/                   # Código fuente de la aplicación
│       ├── src/                   # Código fuente y recursos
├── pom.xml                        # Archivo de configuración de Maven
├── sql/                           # Archivos SQL
│   ├── schema.sql                 # Esquema de base de datos
```

## Configuración

### Base de Datos

El archivo `docker-compose.yml` configura un contenedor MySQL y PhpMyAdmin.  
El esquema de la base de datos se encuentra en `sql/schema.sql` y se carga automáticamente al iniciar el contenedor MySQL.

### Conexión a la Base de Datos

La configuración de la conexión está en la clase `MyDataSource`. Asegúrate de que los valores de `JdbcUrl`, `Username` y `Password` coincidan con los definidos en `docker-compose.yml`.

## Iniciar Servicios

1. Para iniciar los contenedores con Docker, ejecuta el siguiente comando:
   ```bash
   docker-compose up -d
   ```

2. Para compilar el proyecto, navega al directorio `java/lab-pmv` y ejecuta:
   ```bash
   mvn clean install
   ```

## Ejecutar la Aplicación

Para ejecutar la aplicación, utiliza el siguiente comando:
```bash
java -jar target/lab-pmv.jar
```

## Uso

La aplicación presenta un menú interactivo con las siguientes opciones:

1. **Listar todas las categorías**
2. **Listar todos los productos**
3. **Obtener categoría por ID**
4. **Obtener producto por ID**
5. **Insertar nueva categoría**
6. **Insertar nuevo producto**
7. **Actualizar categoría**
8. **Actualizar producto**
9. **Eliminar categoría**
10. **Eliminar producto**
11. **Salir**

## Comandos Docker

### Iniciar contenedores:

```bash
docker-compose up -d
```

### Detener contenedores:

```bash
docker-compose down
```

### Ver logs:

```bash
docker-compose logs -f
```

## Características

- Gestión de categorías y productos con operaciones CRUD.
- Validación para evitar la creación de productos sin categorías.
- Listado de categorías disponibles al insertar o actualizar productos.
- Uso de HikariCP para la gestión eficiente de conexiones a la base de datos.

## Tecnologías Utilizadas

- **Java 17**: Lenguaje de programación principal.
- **Maven**: Herramienta de construcción y gestión de dependencias.
- **MySQL**: Base de datos relacional.
- **HikariCP**: Pool de conexiones para MySQL.
- **Docker**: Contenedores para MySQL y PhpMyAdmin.
- **JUnit 5**: Framework de pruebas unitarias.

## Autor

Este proyecto fue desarrollado por Pol Miret Vidal como parte de un ejercicio práctico para aprender sobre **Java**, **MySQL** y **Docker**.
