FROM python:3.12-slim

# instalaciones
RUN pip install neo4j pandas

# copiar el archivo.py en el contenedor
COPY procesamiento_de_datos.py /app/procesamiento_de_datos.py

# directorio de trabajo
WORKDIR /app

# ejecutar el archivo
CMD ["python", "procesamiento_de_datos.py"]