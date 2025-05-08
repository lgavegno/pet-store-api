
# 🐾 Peluquería Canina

Este proyecto es una aplicación de escritorio Java que permite la gestión de una base de datos para una peluquería canina. Implementa funcionalidades CRUD (Crear, Leer, Actualizar y Borrar) sobre entidades `Mascota` y `Dueño`, utilizando JPA (EclipseLink) y una base de datos MySQL.

---

## 📦 Tecnologías Utilizadas

* **Java 17**
* **Maven**
* **JPA (EclipseLink 2.7.12)**
* **MySQL Connector/J (8.1.0)**
* **Jakarta Persistence 2.2.3**

---

## 🧩 Estructura del Proyecto

```
src/
├── controller/
│   ├── Controladora.java         # Lógica de negocio
│   ├── Duenio.java               # Entidad Dueño
│   └── Mascota.java              # Entidad Mascota
├── model/
│   ├── ControladoraPersistencia.java  # Controladora JPA (no incluida en este README)
│   └── DuenioJpaController.java       # Controlador JPA para Dueño
└── resources/
    └── META-INF/persistence.xml       # Configuración de la unidad de persistencia
```

---

## ✅ Funcionalidades

* Crear una nueva mascota con su dueño asociado.
* Listar todas las mascotas registradas.
* Eliminar una mascota por su número de cliente.
* Modificar datos de una mascota y su dueño.
* Buscar una mascota por su número de cliente.

---

## 🛠️ Configuración del Proyecto

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/peluqueria-canina.git
cd peluqueria-canina
```

### 2. Configurar la base de datos MySQL

Crea una base de datos en MySQL, por ejemplo:

```sql
CREATE DATABASE peluqueria_canina;
```

Configura `persistence.xml` con tus credenciales de conexión:

```xml
<property name="javax.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/peluqueria_canina"/>
<property name="javax.persistence.jdbc.user" value="root"/>
<property name="javax.persistence.jdbc.password" value="tu_contraseña"/>
```

### 3. Compilar y ejecutar

```bash
mvn clean install
```

Puedes ejecutar la aplicación desde tu IDE o con:

```bash
mvn exec:java
```

---

## 📄 Clases Principales

### `Controladora.java`

Clase principal de lógica. Coordina la creación, edición, eliminación y recuperación de mascotas y sus dueños.

### `Mascota.java`

Entidad JPA con atributos como nombre, raza, color, alergias, atención especial y relación con un dueño.

### `Duenio.java`

Entidad JPA con atributos como nombre y teléfono.

### `DuenioJpaController.java`

Controlador JPA para operaciones de persistencia de la entidad `Duenio`.

---

## 🧪 Pruebas

Actualmente, el proyecto no incluye pruebas automatizadas. Puedes agregar pruebas JUnit para validar el comportamiento de la lógica en `Controladora`.

---

## 📌 Notas
* El crédito es del canal TODOCODE.
* Asegúrate de tener el servidor MySQL corriendo antes de ejecutar la aplicación.
* Las relaciones entre entidades están definidas con anotaciones JPA (`@OneToOne`).
* El campo `num_cliente` es generado automáticamente como ID de la mascota.

---


