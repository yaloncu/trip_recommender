# Etapa de construcción
FROM node:18 AS build

WORKDIR /app

# Instalar las dependencias
COPY frontend/package*.json ./
#RUN npm install
RUN npm install

# Copiar el código fuente y construye la aplicación
COPY frontend/ ./
RUN npm run build

# Etapa de produccións
FROM nginx:stable

# Copiar los archivos generados al servidor Nginx
COPY --from=build /app/dist /usr/share/nginx/html

# Copiar el archivo de configuración personalizado de Nginx
COPY frontend/nginx.conf /etc/nginx/conf.d/default.conf

# Exponer el puerto 80
EXPOSE 80

# Iniciar nginx
CMD ["nginx", "-g", "daemon off;"]
