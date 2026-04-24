# ProjectDates - API de Reservas con Control de Concurrencia

API de reservas desarrollada en Java diseñada para resolver desafíos críticos de backend, específicamente la gestión de concurrencia en sistemas de turnos masivos. El sistema implementa una arquitectura desacoplada y mecanismos de bloqueo distribuido para evitar colisiones de datos y sobre-reservas.

## Problema y Solución Técnica

En sistemas de alta demanda, múltiples peticiones concurrentes pueden intentar reservar el mismo bloque horario simultáneamente. Si no se controla, esto genera inconsistencias de datos y overbooking.

La solución implementada utiliza una estrategia de doble validación:
1.  **Bloqueo Distribuido con Redis**: Antes de procesar cualquier reserva, se intenta adquirir un bloqueo único para el slot y fecha específicos. Esto reduce significativamente las colisiones concurrentes.
2.  **Integridad en Base de Datos**: Como fuente de verdad final, PostgreSQL aplica restricciones de unicidad y transaccionalidad para garantizar que, incluso ante un fallo en la capa de cache, la base de datos permanezca consistente.

---

## Arquitectura del Sistema

El proyecto sigue los principios de Clean Architecture para garantizar mantenibilidad y facilidad de testing:

-   **Core**: Contiene las entidades de dominio, lógica de negocio central e interfaces de repositorios. No tiene dependencias externas.
-   **Infrastructure**: Implementaciones concretas de persistencia (SQL), cache (Redis) y comunicación HTTP.
-   **Desacoplamiento**: La lógica de negocio se comunica con la infraestructura mediante interfaces, permitiendo cambiar de tecnologías (ej: de almacenamiento local a S3) sin afectar el núcleo del sistema.

---

## Documentación de API

Todos los endpoints bajo `/api/*` requieren autenticación mediante JWT, a excepción de los métodos de registro y login.

### Flujo de creación de reserva

1. Se recibe la petición de reserva
2. Se intenta adquirir un lock en Redis para (concept_id + fecha + hora)
3. Si el lock falla → se responde 409 Conflict
4. Si el lock es exitoso:
   - Se valida disponibilidad en PostgreSQL
   - Se intenta crear la reserva
5. Si la inserción falla por constraint → 409 Conflict
6. Se libera el lock
7. Se devuelve la respuesta


### Autenticación
-   `POST /api/auth`: Inicio de sesión. Devuelve un token JWT para peticiones posteriores.

### Gestión de Usuarios (`/api/users`)
-   `POST /register`: Registro de nuevos usuarios.
-   `GET /`: Listado completo de usuarios registrados.
-   `GET /{email}`: Búsqueda de un usuario específico por su dirección de correo.
-   `PATCH /{id}`: Actualización parcial de datos de usuario. (Requiere permiso: UPDATE_USER).
-   `DELETE /{id}`: Eliminación física de un usuario del sistema.

### Gestión de Conceptos (`/api/concept`)
Representan los servicios o locales que ofrecen turnos (ej: canchas, consultorios).
-   `POST /`: Creación de un nuevo concepto. Soporta `multipart/form-data` para recibir metadatos y una imagen de perfil.
-   `GET /`: Obtiene todos los conceptos activos y sus horarios disponibles.
-   `GET /{id}`: Detalle completo de un concepto específico, incluyendo su imagen y slots.
-   `PATCH /{id}`: Modificación de parámetros del concepto. (Requiere permiso: UPDATE_CONCEPT).
-   `DELETE /{id}`: Eliminación de un concepto y sus horarios asociados. (Requiere permiso: DELETE_CONCEPT).
w
### Reservas y Turnos (`/api/reservation`)
-   `POST /`: Creación de una reserva. Este endpoint utiliza Redis para el control de concurrencia y valida la disponibilidad del slot en tiempo real. (Requiere permiso: CREATE_SLOT).
-   `GET /`: Consulta de todas las reservas realizadas en el sistema.

---

## Configuración e Instalación

### Requisitos
-   Java 21
-   PostgreSQL
-   Redis
-   Maven

### Configuración de Entorno
Cree un archivo `.env` en la raíz del proyecto con las siguientes variables:

```env
DB_URL=jdbc:postgresql://localhost:5432/nombre_db
DB_USER=usuario
DB_PASS=contraseña
REDIS_HOST=localhost
REDIS_PORT=6379
