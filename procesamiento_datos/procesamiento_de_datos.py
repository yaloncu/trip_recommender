from neo4j import GraphDatabase
import pandas as pd


URI = "bolt://neo4j:7687"
USERNAME = "neo4j"
PASSWORD = "12345678"

# función crea nodos de destinos
def create_destination_node(tx,nombre_destino, pais, tipo_vacacion, publico_dirigido):
    tx.run("CREATE (:Destino {nombre_destino: $nombre_destino, pais: $pais, tipo_vacacion: $tipo_vacacion, publico_dirigido: $publico_dirigido})", nombre_destino=nombre_destino, pais=pais, tipo_vacacion=tipo_vacacion, publico_dirigido=publico_dirigido)

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
        session.execute_write(create_destination_node, row['nombre_destino'], row['pais'], row['tipo_vacacion'], row['publico_dirigido'])

# cerrar driver
driver.close()