from neo4j import GraphDatabase
import pandas as pd

URI = "bolt://neo4j:7687"
USERNAME = "neo4j"
PASSWORD = "12345678"

# función que crea nodos de destinos
def create_destination_node(tx, nombre_destino, pais, tipo_vacacion, publico_dirigido):
    tx.run("""
    MERGE (d:Destino {nombre_destino: $nombre_destino})
    ON CREATE SET d.pais = $pais, d.tipo_vacacion = $tipo_vacacion, d.publico_dirigido = $publico_dirigido
    """, nombre_destino=nombre_destino, pais=pais, tipo_vacacion=tipo_vacacion, publico_dirigido=publico_dirigido)

# driver de conexión
driver = GraphDatabase.driver(URI, auth=(USERNAME, PASSWORD))

# verificar conexión
with driver.session() as session:
    session.run("RETURN 1")
    print("Conectado a Neo4j!")

# leer el CSV
df = pd.read_csv('/datos/destinos_vacaciones_europa.csv')

# conectar y ejecutar
with driver.session() as session:
    for index, row in df.iterrows():
        # Convertir la columna tipo_vacacion en un array separando por ";"
        tipo_vacacion_array = row['tipo_vacacion'].split(';')

        # Convertir la columna publico_dirigido en un array separando por ";"
        publico_dirigido_array = row['publico_dirigido'].split(';')
        
        # Ejecutar la creación del nodo
        session.execute_write(create_destination_node, row['nombre_destino'], row['pais'], tipo_vacacion_array, publico_dirigido_array)

# cerrar driver
driver.close()
