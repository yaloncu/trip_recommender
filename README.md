# CDSGarage - Proyecto de Gestión de Grupos y Recomendación de Destinos de Viaje

## Objetivos

El objetivo principal de este proyecto es facilitar la organización de viajes en grupo mediante:

1. **Encontrar fechas comunes:** Utilizando algoritmos para calcular las fechas que mejor se adapten a todos los participantes.

2. **Recomendar destinos ideales:** Basándose en las preferencias colectivas de los usuarios.

3. **Conectar compañeros de viaje:** Permitiendo a los usuarios unirse a grupos con intereses similares o formar grupos privados con sus conocidos.

## Descripción del Proyecto
Este proyecto consiste en una aplicación web basada en microservicios para la organización de viajes en grupo. La aplicación está dirigida a usuarios interesados en realizar viajes grupales y que necesitan ayuda para encontrar compañeros, fechas comunes y un destino ideal. Además, fomenta la conexión entre personas con intereses similares mediante grupos públicos y privados.

### Características Principales
1. **Gestor de Grupos:**
   - **Grupos Públicos:** Cualquier usuario puede unirse y el administrador elige las fechas y el destino del viaje.
   - **Grupos Privados:** Solo accesibles mediante invitación, con votación para decidir fechas y destino.
2. **Recomendación de Destinos:** Basada en las preferencias de los usuarios.
3. **Cálculo de Fechas Óptimas:** Uso de algoritmos como "Sliding Window" para encontrar las fechas que mejor se adapten a los participantes.
4. **Internacionalización:** La aplicación está disponible en español, inglés, francés y alemán.
5. **Inicio de sesión con Google:** Para facilitar el acceso de los usuarios.

## Arquitectura del Sistema
La aplicación se implementó utilizando una arquitectura basada en microservicios, distribuidos en diferentes contenedores Docker:

1. **Origen de Datos:** Contenedor con un CSV inicial de destinos potenciales.
2. **Procesamiento de Datos:** Contenedor que procesa el CSV e inserta los datos en la base de datos utilizando Python.
3. **Base de Datos:** Contenedor con Neo4j para la gestión de datos.
4. **Frontend:** Desarrollado con Vue.js y servido mediante Nginx.
5. **Backend:** Implementado con Java y Spring Boot para manejar la lógica del negocio.
6. **Chatbot:** Basado en Rasa para asistir a los usuarios en la organización de viajes.

## Instrucciones de Instalación y Despliegue

### Requisitos Previos
- Docker y Docker Compose instalados.

### Pasos de Instalación
1. Clona el repositorio:
   ```bash
   git clone https://github.com/cdsgarage/cdsgarage.git
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

## Futuros Desarrollos
- Integración de filtros de búsqueda avanzada para grupos.
- Extensión de funcionalidades del chatbot.
- Mejora en los algoritmos de recomendación de destinos.


