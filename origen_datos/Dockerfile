FROM ubuntu:latest 

# instalaciones
RUN apt-get update && apt-get install -y bash

# directorios
RUN mkdir -p /datos

# copiar csv en el contenedor
COPY datos/destinos_vacaciones_europa.csv /datos/destinos_vacaciones_europa.csv

# directorio de trabajo
WORKDIR /datos

# puerto
EXPOSE 8080