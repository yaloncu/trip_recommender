FROM python:3.12-slim

# instalaciones
RUN pip install neo4j pandas

# copiar el archivo.py en el contenedor
COPY procesamiento_de_datos.py /app/procesamiento_de_datos.py
COPY procesamiento_grupos_usuarios.py /app/procesamiento_grupos_usuarios.py

# directorio de trabajo
WORKDIR /app

# ejecutar el archivo
CMD ["sh", "-c", "sleep 20 && python procesamiento_de_datos.py && python procesamiento_grupos_usuarios.py"]
