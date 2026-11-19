# 🖥️ MPAZ Pañol — Backend Microservicios REST (Spring Boot)

**Asignatura:** Desarrollo de Aplicaciones Móviles (DSY1105)  
**Sección:** 010D  
**Examen Transversal (ET) / Experiencia 3** — Escuela de Informática y Telecomunicaciones  
**Institución:** Duoc UC  

---

## 👥 Datos de la Entrega
* **Estudiante Integrante:** Emilio Campillay Soliz
* **Docente:** Viviana Soledad Poblete López
* **Sección:** 010D
* **Asignatura:** Desarrollo de Aplicaciones Móviles (DSY1105)
* **Correo Institucional Docente:** `vpobletel@profesor.duoc.cl`

---

## 📄 Descripción del Proyecto
Este repositorio contiene la arquitectura de **Microservicios REST** desarrollada con **Java y Spring Boot**, que provee los servicios web para la aplicación móvil Android **MPAZ Pañol** (Sistema de Préstamos e Inventario Tecnológico).

El backend se conecta a una base de datos relacional **PostgreSQL alojada en la nube (Supabase Cloud)**, gestiona la persistencia de datos mediante **Spring Data JPA / Hibernate**, valida las reglas críticas de negocio y expone endpoints seguros consumidos por el cliente Android a través de **Retrofit**.

---

## 🛠️ Tecnologías Utilizadas

| Componente | Tecnología |
| :--- | :--- |
| **Lenguaje** | Java 17 / 21 / 23 |
| **Framework** | Spring Boot 3.3.5 |
| **Capa Web** | Spring Web (Controladores REST con `@RestController` y CORS) |
| **Persistencia** | Spring Data JPA / Hibernate |
| **Base de Datos Cloud** | PostgreSQL 15+ hospedado en **Supabase Cloud** |
| **Pruebas Unitarias** | **JUnit 5** y **Mockito** (Pruebas aisladas de la capa de servicio) |
| **Gestores de Construcción** | Maven (`pom.xml`) y Gradle (`build.gradle`) con wrapper incluido |

---

## 🏛️ Arquitectura del Sistema (Capas)

```text
com.mpaz.panol
├── controller       # Capa de Exposición: EquipoController, SolicitudController, UsuarioController
├── service          # Capa de Negocio: EquipoService, SolicitudService, UsuarioService
├── repository       # Capa de Persistencia: EquipoRepository, SolicitudRepository, UsuarioRepository (JPA)
├── entity           # Modelos de Datos: Equipo, Solicitud, Usuario
└── MpazPanolApplication.java # Punto de entrada principal Spring Boot
```

---

## 🌐 Catálogo de Endpoints REST

### 1. Equipos e Inventario (`/api/equipos`)
* `GET /api/equipos`: Lista todos los equipos (permite filtro opcional `?categoria=Robótica`).
* `GET /api/equipos/{id}`: Obtiene el detalle de un equipo por su ID.
* `POST /api/equipos`: Registra un nuevo equipo con ubicación física en el pañol.
* `PUT /api/equipos/{id}`: Actualiza stock o estado de un equipo existente.
* `DELETE /api/equipos/{id}`: Elimina un equipo por ID.

### 2. Solicitudes de Préstamo (`/api/solicitudes`)
* `GET /api/solicitudes`: Obtiene todas las solicitudes ordenadas cronológicamente para la consola de TI.
* `GET /api/solicitudes/docente?email={correo}`: Obtiene las solicitudes específicas de un docente.
* `POST /api/solicitudes`: Crea una nueva solicitud validando la **Regla Crítica de 3 horas de anticipación**.
* `PATCH /api/solicitudes/{id}/estado?estado={nuevoEstado}&justificacion={motivo}`: Aprobación o rechazo con **justificación obligatoria**.

### 3. Autenticación (`/api/usuarios`)
* `POST /api/usuarios/login`: Valida credenciales contra la base de datos PostgreSQL.
* `POST /api/usuarios/registro`: Registra un nuevo docente en el sistema.

---

## 🧪 Pruebas Unitarias Backend (JUnit 5 + Mockito)

Cumpliendo con el requerimiento de la Experiencia 3 y ET, se implementaron pruebas unitarias completas para los servicios de negocio:

* **`SolicitudServiceTest`:**
  1. `crearSolicitud_conMenosDe3HorasAnticipacion_lanzaExcepcion`: Valida que el backend rechace solicitudes que no cumplan la regla de 3 horas.
  2. `crearSolicitud_conAnticipacionValida_persisteExitosamente`: Verifica la generación automática de código de ticket y estado "Pendiente".
  3. `cambiarEstado_aRechazadaSinJustificacion_lanzaExcepcion`: Garantiza que el rechazo requiera obligatoriamente una justificación escrita.
  4. `cambiarEstado_aRechazadaConJustificacion_actualizaEstado`: Comprueba la persistencia del motivo de rechazo.
  5. `cambiarEstado_aAprobada_actualizaEstadoTicket`: Valida el paso a estado "Aceptada".
* **`EquipoServiceTest`:**
  1. `listarTodos_retornaListaDeEquipos`: Valida la consulta al repositorio.
  2. `guardar_sinCodigo_lanzaExcepcion`: Valida campos obligatorios.
  3. `guardar_stockDisponibleMayorATotal_ajustaDisponible`: Corrige inconsistencias de stock automáticamente.
  4. `eliminar_idInexistente_lanzaExcepcion`: Manejo de excepciones en bajas inexistentes.

### Ejecución de Pruebas:
```bash
# Con Gradle Wrapper:
.\gradlew.bat test

# Con Maven:
mvn test
```

---

## 🚀 Instrucciones para Levantar el Backend

1. **Configurar la Contraseña de Supabase:**
   En `src/main/resources/application.properties`, colocar la contraseña de tu base de datos Supabase o definir la variable de entorno:
   ```powershell
   $env:DB_PASSWORD="tu_password_supabase"
   ```

2. **Iniciar la Aplicación:**
   ```bash
   # En Windows:
   .\gradlew.bat bootRun

   # O con Maven:
   mvn spring-boot:run
   ```

3. El backend quedará escuchando en el puerto `8080`: `http://localhost:8080` (accesible desde el emulador Android mediante `http://10.0.2.2:8080`).
