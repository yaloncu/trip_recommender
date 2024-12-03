FROM rasa/rasa:3.5.3

# Copiar archivos de Rasa
COPY . /app

# Configurar el directorio de trabajo
WORKDIR /app

# Eliminar modelos antiguos
RUN rm -rf models/*

# Instalar dependencias si existen
COPY requirements.txt /app/requirements.txt
RUN pip install -r /app/requirements.txt

# Entrenamiento del modelo
RUN rasa train -vv --dump-stories --force

# Iniciar el servidor
CMD ["rasa", "run", "--enable-api", "--cors", "*"]
