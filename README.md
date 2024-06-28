# Banking System

Este proyecto consiste en un sistema de microservicios para manejar la información de clientes y productos financieros. Está compuesto por tres microservicios principales: `customer-service`, `product-service`, y `bff-service`.

## Estructura del Proyecto

project-root/
├── customer-service/
│   ├── src/
│   ├── target/
│   ├── Dockerfile
│   └── pom.xml
├── product-service/
│   ├── src/
│   ├── target/
│   ├── Dockerfile
│   └── pom.xml
├── bff-service/
│   ├── src/
│   ├── target/
│   ├── Dockerfile
│   └── pom.xml
└── docker-compose.yml
Requisitos Previos
Java 11
Maven 3.6+
Docker
Docker Compose
Configuración
Variables de Entorno
Asegúrate de definir las siguientes variables de entorno en tu entorno o en un archivo .env en la raíz del proyecto:

DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
OAUTH_JWK_SET_URI=https://auth-server.com/oauth/jwks
Construcción del Proyecto
Navega a la raíz de cada microservicio y ejecuta:
mvn clean install
Construcción y Ejecución con Docker
Para construir y ejecutar todos los microservicios utilizando Docker Compose, asegúrate de estar en la raíz del proyecto y ejecuta:

docker-compose build
docker-compose up
Endpoints de la API
customer-service
GET /customers/{uniqueCode}: Obtiene la información del cliente por su código único.
product-service
GET /products/customer/{customerId}: Obtiene los productos financieros del cliente por su ID.
bff-service
GET /bff/{uniqueCode}: Obtiene la información del cliente y sus productos financieros por su código único.

Acceder a la Documentación de la API
Después de configurar los archivos necesarios, puedes acceder a la documentación de la API generada automáticamente por Swagger en las siguientes URL:

Customer Service: http://localhost:8081/swagger-ui.html
Product Service: http://localhost:8082/swagger-ui.html
BFF Service: http://localhost:8083/swagger-ui.html
Resumen
Pruebas Unitarias
Las pruebas unitarias están implementadas utilizando JUnit y StepVerifier para pruebas reactivas.

Ejecutar Pruebas Unitarias
Para ejecutar las pruebas unitarias, navega a la raíz de cada microservicio y ejecuta:

mvn test
Manejo de Errores
El proyecto incluye un manejador de errores global (GlobalErrorWebFilter) que intercepta todas las excepciones y las transforma en un DTO de error (ErrorResponse) de cara a la respuesta de la API.

Logging
El sistema utiliza Logback para el manejo de logs, incluyendo la propagación de traceId para el seguimiento de peticiones a través de los microservicios.