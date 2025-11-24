Manual de Uso Profesional – Codiplayco
Este manual explica el uso normal y profesional de Codiplayco, una aplicación web desarrollada con Java Spring Boot, Thymeleaf, HTML, CSS y JavaScript. Está dirigido tanto a usuarios finales como a personal técnico que busca operar la aplicación de forma efectiva.

1. Descripción del Sistema
Codiplayco es una plataforma web construida con Spring Boot (Java 17), que gestiona recursos estáticos, plantillas HTML interactivas, y persistencia usando base de datos MySQL. El sistema emplea Thymeleaf para vistas, CSS para estilos y JavaScript para interactividad.

2. Requisitos Previos
Java 17 o superior instalado
Maven instalado (puede usarse el wrapper incluido: mvnw/mvnw.cmd)
Acceso a una base de datos MySQL y credenciales de configuración
Navegador web actualizado
3. Instalación y Configuración
Instalación
Clonar el repositorio:

bash
git clone https://github.com/julianriver1302/Codiplayco.git
cd Codiplayco
Configurar base de datos:

Editar el archivo src/main/resources/application.properties y establecer tus datos de conexión MySQL:
Code
spring.datasource.url=jdbc:mysql://localhost:3306/codiplayco
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA
Compilar el proyecto:

En Windows:
Code
mvnw.cmd clean install
En Linux/Mac:
Code
./mvnw clean install
Iniciar la aplicación:

bash
./mvnw spring-boot:run
o

bash
java -jar target/codiPlayCo-0.0.1-SNAPSHOT.jar
Acceso
Por defecto, la aplicación está disponible en:
http://localhost:8080
4. Uso Normal
Acceso al sistema
Abre tu navegador y dirígete a http://localhost:8080
Navegación básica
Interactúa con la web usando los menús y botones disponibles.
Carga y manipula archivos si la función lo permite (consultar carpeta "uploads" en el repositorio).
Utiliza las diferentes vistas provistas por el sistema (HTML y Thymeleaf).
5. Uso Profesional (Administración y explotación avanzada)
Gestión de base de datos:
Administra usuarios, datos y configuraciones directamente en MySQL si eres administrador.
Personalización de vistas:
Modifica archivos en src/main/resources/templates para cambiar las páginas HTML.
Estilos y recursos en src/main/resources/static (HTML, CSS, JS).
Configuración avanzada:
Ajusta parámetros en application.properties (puertos, credenciales, paths, etc).
Desarrollo y extensión:
El código fuente Java se encuentra en src/main/java/com/... (módulos, controladores, entidades).
Estructura Spring Boot: sigue la convención de controladores, servicios, repositorios y entidades.
6. Solución de problemas
Error de arranque: Revisa application.properties (usuario/db), asegúrate que MySQL esté disponible.
Problemas front-end: Actualiza el navegador, revisa los archivos en templates y static.
Fallos en Maven: Usa el wrapper incluido, verifica dependencias en pom.xml.
7. Contacto y comunidad
Accede al repositorio en GitHub para soporte, cambios y preguntas.
Utiliza los issues del proyecto para reportar errores y sugerencias.
8. Estructura Importante del Proyecto
src/main/java/com/ – Código fuente Java (backend, lógica de negocio)
src/main/resources/templates – Vistas HTML usando Thymeleaf
src/main/resources/static – Recursos estáticos (imágenes, JS, CSS)
uploads/ – Soporte para archivos subidos por usuarios
pom.xml – Configuración Maven y dependencias
