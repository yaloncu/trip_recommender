# Proyecto de Gestión de Grupos y Recomendación de Destinos de Viaje

## Objetivos

El objetivo principal de esta aplicación web es facilitar la organización colaborativa de viajes y actividades grupales, optimizando la planificación conjunta, la toma de decisiones y la interacción entre personas con intereses comunes.

1. **Crear y gestionar grupos de viaje, con dos modalidades:** 
   - **Grupos públicos**, accesibles a cualquier usuario registrado, pensados para socializar y conectar con nuevas personas.
   - **Grupos privados**, reservados para círculos cerrados de amigos o familiares, mediante invitación.


2. **Proponer y votar fechas y destinos**:
  - En grupos privados, los usuarios introducen sus intervalos de disponibilidad.
  - Un algoritmo eficiente propone automáticamente las fechas óptimas para todos.
  - Posteriormente, se habilita la votación de destinos sugeridos por los participantes.

3. **Crear actividades públicas**, definidas con lugar, fecha y hora, a las que cualquier usuario puede unirse directamente sin necesidad de coordinación adicional.

4. **Comunicarse en tiempo real** gracias a un sistema de mensajería integrado en cada grupo, lo que facilita la coordinación y el intercambio ágil de información.

5. **Utilizar la aplicación en distintos idiomas** (español, inglés, francés y alemán), gracias a su soporte multilingüe e interfaz accesible conforme a la normativa europea.

## Descripción del Proyecto
Este proyecto consiste en una aplicación web basada en microservicios para la organización de viajes en grupo. La aplicación está dirigida a usuarios interesados en realizar viajes grupales y que necesitan ayuda para encontrar compañeros, fechas comunes y un destino ideal. Además, fomenta la conexión entre personas con intereses similares mediante grupos públicos y privados y actividades públicas.

## Arquitectura del Sistema
La aplicación se implementó utilizando una arquitectura basada en microservicios, distribuidos en diferentes contenedores Docker:

1. **Origen de Datos:** Contenedor con un CSV inicial de destinos potenciales.
2. **Procesamiento de Datos:** Contenedor que procesa el CSV e inserta los datos en la base de datos utilizando Python.
3. **Base de Datos:** Contenedor con Neo4j para la gestión de datos.
4. **Frontend:** Desarrollado con Vue.js y servido mediante Nginx.
5. **Backend:** Implementado con Java y Spring Boot para manejar la lógica del negocio.

## Instrucciones de Instalación y Despliegue

### Requisitos Previos
- Docker y Docker Compose instalados.

### Pasos de Instalación
1. Clona el repositorio:
   ```bash
   git clone https://github.com/yaloncu/trip_recommender.git
   cd cdsgarage/projects/ule/2024_2025/mi_proyecto
   ```

2. Construye los contenedores Docker:
   ```bash
   docker-compose up --build
   ```

3. Accede a la aplicación en tu navegador en [http://localhost:8080](http://localhost:8080).

## Ejemplo de Uso
1. **Crear un Grupo:**
   - Ingresa a la aplicación y crea un grupo público o privado.
   - Agrega información sobre el interés del viaje.

2. **Unirse a un Grupo:**
   - Busca un grupo público o acepta una invitación a un grupo privado.

3. **Sugerir Preferencias:**
   - Propón fechas y destinos según tus preferencias.

4. **Revisión del Administrador:**
   - El administrador cierra el grupo y los usuarios votan por el destino final.
