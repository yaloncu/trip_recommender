#!/bin/bash

# arrancar docker base de datos
echo "Iniciando Neo4j..."
docker-compose -f docker-compose-bd.yml up -d

if [ $? -ne 0 ]; then
  echo "Error al iniciar Neo4j."
  exit 1
fi

echo "Esperando a que la base de datos esté disponible..."

while ! docker exec neo4j cypher-shell -u neo4j -p 12345678 "RETURN 1" &>/dev/null; do
    sleep 2
    echo -n "."
done

echo "La base de datos está lista."

# arrancar docker app
docker-compose -f docker-compose-app.yml up -d
