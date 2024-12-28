Proyecto de Gestión de Grupos y Recomendación de Destinos de Viaje

Descripción del Proyecto

Este proyecto consiste en una aplicación web basada en microservicios para la organización de viajes en grupo. La aplicación está dirigida a usuarios interesados en realizar viajes grupales y que necesitan ayuda para encontrar compañeros, fechas comunes y un destino ideal. Además, fomenta la conexión entre personas con intereses similares mediante grupos públicos y privados.

Características Principales

Gestor de Grupos:

Grupos Públicos: Cualquier usuario puede unirse y el administrador elige las fechas y el destino del viaje.

Grupos Privados: Solo accesibles mediante invitación, con votación para decidir fechas y destino.

Recomendación de Destinos: Basada en las preferencias de los usuarios.

Cálculo de Fechas Óptimas: Uso de algoritmos como "Sliding Window" para encontrar las fechas que mejor se adapten a los participantes.

Internacionalización: La aplicación está disponible en español, inglés, francés y alemán.

Inicio de sesión con Google: Para facilitar el acceso de los usuarios.

Arquitectura del Sistema

La aplicación se implementó utilizando una arquitectura basada en microservicios, distribuidos en diferentes contenedores Docker:

Origen de Datos: Contenedor con un CSV inicial de destinos potenciales.

Procesamiento de Datos: Contenedor que procesa el CSV e inserta los datos en la base de datos utilizando Python.

Base de Datos: Contenedor con Neo4j para la gestión de datos.

Frontend: Desarrollado con Vue.js y servido mediante Nginx.

Backend: Implementado con Java y Spring Boot para manejar la lógica del negocio.

Chatbot: Basado en Rasa para asistir a los usuarios en la organización de viajes.

Instrucciones de Instalación y Despliegue

Requisitos Previos

Docker y Docker Compose instalados.

Acceso al repositorio del proyecto.

Pasos de Instalación

Clona el repositorio:

git clone https://github.com/cdsgarage/cdsgarage.git
cd cdsgarage/projects/ule/2024_2025/mi_proyecto

Construye los contenedores Docker:

docker-compose up --build

Accede a la aplicación en tu navegador en http://localhost:8080.