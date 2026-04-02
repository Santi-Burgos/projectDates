# ProjectDates - API de Reservas

Este es un proyecto de práctica desarrollado en **Java "Vanilla"** (sin grandes frameworks como Spring Boot) para explorar la creación de una API robusta y, específicamente, abordar el reto de la **concurrencia** en sistemas de reservas.

El objetivo principal es resolver el problema clásico de: *¿Qué sucede cuando dos personas intentan reservar el mismo slot exactamente al mismo tiempo?* (El problema del "doble clic").

## Tecnologías Utilizadas

- **Java 21**: Aprovechando las últimas funcionalidades del lenguaje.
- **Maven**: Gestión de dependencias y ciclo de vida del proyecto.
- **Javalin (v7.0.0)**: Servidor HTTP ligero y moderno.
- **PostgreSQL**: Base de datos relacional para persistencia.
- **Flyway**: Control de versiones y migraciones de la base de datos.
- **HikariCP**: Pool de conexiones de alto rendimiento.
- **BCrypt**: Seguridad y hashing de contraseñas.
- **Dotenv**: Gestión de variables de entorno.

## Requisitos Previos

1. **Java SDK 21** instalado.
2. **Maven 3.x** instalado.
3. Instancia de **PostgreSQL** corriendo.

## ⚙️ Configuración

Crea un archivo `.env` en la raíz del proyecto basado en el siguiente ejemplo:

```env
DB_URL=jdbc:postgresql://localhost:5432/nombre_tu_db
DB_USER=tu_usuario
DB_PASS=tu_password
DB_MAX_POOL=10
```

## Base de Datos

El proyecto utiliza **Flyway** para gestionar el esquema. Al iniciar la aplicación, las migraciones se ejecutarán automáticamente. El esquema incluye:

- `users` y `roles`: Gestión de usuarios y permisos.
- `concept`: Los servicios o items que se pueden reservar (ej. "Cancha 1", "Doctor X").
- `slot`: Definición de horarios disponibles.
- `appointments`: Las reservas realizadas.

## 🏃 Ejecución

Para iniciar el servidor, utiliza el siguiente comando de Maven:

```bash
mvn exec:java
```

El servidor se iniciará por defecto en `http://localhost:8000`.

## El Desafío de Concurrencia

El corazón de este proyecto es implementar mecanismos para evitar la sobre-reserva (*overbooking*). Estamos practicando diferentes enfoques:

1.  **Optimistic Locking**: Usando versiones o timestamps para detectar colisiones.
2.  **Pessimistic Locking**: Bloqueos a nivel de base de datos (`SELECT FOR UPDATE`).
3.  **Database Constraints**: Aprovechar la integridad referencial y restricciones `UNIQUE` o `EXCLUSION` de PostgreSQL.

## Estructura del Proyecto

Siguiendo una arquitectura limpia y modular:

- `core`: Entidades de dominio, servicios de negocio y definiciones de repositorios.
- `infrastructure`: Implementaciones de persistencia (SQL), configuración de red y controladores HTTP.
- `resources/db/migration`: Scripts SQL de Flyway.
