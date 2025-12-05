📘 Codiplayco – Manual de Uso Profesional

Codiplayco es una aplicación web desarrollada con Java Spring Boot, Thymeleaf, HTML, CSS y JavaScript, diseñada para la gestión de contenidos y la interacción con archivos. Este documento sirve como guía tanto para el uso normal como para la administración técnica del sistema.

🚀 1. Descripción del Sistema

Codiplayco es una plataforma web construida con:

Java 17

Spring Boot

Thymeleaf para vistas dinámicas

MySQL como base de datos

HTML, CSS y JavaScript para el frontend

La aplicación organiza recursos estáticos, controladores, plantillas y lógica de negocio bajo la arquitectura estándar de Spring Boot.

🧩 2. Requisitos Previos

Para ejecutar el proyecto necesitas:

Java 17 o superior

Maven instalado (o usar el wrapper mvnw / mvnw.cmd)

Acceso a una base de datos MySQL

Un navegador web actualizado

🛠️ 3. Instalación y Configuración
📥 Clonar el repositorio
git clone https://github.com/julianriver1302/Codiplayco.git
cd Codiplayco

🗄️ Configurar la base de datos

Edita el archivo:

src/main/resources/application.properties


Ejemplo:

spring.datasource.url=jdbc:mysql://localhost:3306/codiplayco
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA

🔧 Compilar el proyecto

Windows:

mvnw.cmd clean install


Linux/Mac:

./mvnw clean install

▶️ Ejecutar la aplicación
./mvnw spring-boot:run


o:

java -jar target/codiPlayCo-0.0.1-SNAPSHOT.jar

🌐 Acceso

Abra su navegador y diríjase a:

http://localhost:8080

🖱️ 4. Uso Normal

Accede a la aplicación desde un navegador.

Navega por los menús y secciones disponibles.

Utiliza las funciones que permiten carga y manipulación de archivos (carpeta uploads).

Interactúa con las vistas generadas por HTML + Thymeleaf.

🧑‍💼 5. Uso Profesional (Administración Avanzada)
📊 Gestión de la base de datos

Administradores pueden modificar:

Usuarios

Configuración

Datos internos del sistema

Directamente sobre MySQL.

🎨 Personalización de vistas

Modificar archivos en:

src/main/resources/templates (HTML + Thymeleaf)

src/main/resources/static (CSS, JS, imágenes)

⚙️ Configuraciones avanzadas

Ajustar:

Puertos

Credenciales

Rutas de subida

Parámetros del servidor

En application.properties.

💻 Extensión del sistema (Desarrollo)

Código fuente principal en:

src/main/java/com/


Sigue la estructura Spring Boot:

Controladores

Servicios

Repositorios

Entidades

🛑 6. Solución de Problemas
❗ Error al iniciar

Revisa credenciales en application.properties.

Confirma que MySQL esté funcionando.

❗ Problemas front-end

Revisa los archivos HTML/JS/CSS.

Limpia la caché del navegador.

❗ Fallos con Maven

Usa el wrapper incluido.

Verifica dependencias en pom.xml.

🤝 7. Contacto y Comunidad

Repositorio en GitHub: (link según corresponda)

Usa la sección Issues para reportar errores, sugerencias o mejoras.

📂 8. Estructura Importante del Proyecto
src/main/java/com/               -> Código fuente (controladores, servicios, entidades)
src/main/resources/templates     -> Plantillas HTML (Thymeleaf)
src/main/resources/static        -> CSS, JS, imágenes
uploads/                         -> Archivos subidos por usuarios
pom.xml                          -> Configuración de dependencias (Maven)
