FROM node:18 
WORKDIR /app

COPY frontend/package*.json ./
RUN ls -al /app

RUN npm install
COPY frontend/ ./
RUN npm run build
EXPOSE 80
CMD ["npm", "run", "serve"]
