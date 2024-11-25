FROM rasa/rasa:3.5.3

# Copiar tus archivos de Rasa
COPY . /app

# Configurar el directorio de trabajo
WORKDIR /app

# Entrenamiento (opcional si no tienes un modelo preentrenado)
RUN rasa train

# Iniciar el servidor
CMD ["rasa", "run", "--enable-api", "--cors", "*"]