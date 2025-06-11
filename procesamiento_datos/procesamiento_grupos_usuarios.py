from neo4j import GraphDatabase
import random
import datetime

URI = "bolt://neo4j:7687"
USERNAME = "neo4j"
PASSWORD = "12345678"

driver = GraphDatabase.driver(URI, auth=(USERNAME, PASSWORD))

# Crear nodos de usuario
def create_user(tx, name, email):
    tx.run("""
    MERGE (u:User {email: $email})
    ON CREATE SET u.name = $name
    """, name=name, email=email)

# Crear grupos públicos según la estructura del backend
def create_public_group(tx, name, email, audience, privated, isClosed, tripType, departureDate, returnDate):
    tx.run("""
    CREATE (g:Group {
        name: $name,
        email: $email,
        audience: $audience,
        privated: $privated,
        isClosed: $isClosed,
        isClosedVoting: false,
        tripType: $tripType,
        departureDate: date($departureDate),
        returnDate: date($returnDate)
    })
    WITH g
    MATCH (u:User {email: $email})
    CREATE (u)-[r:PERTENECE_A]->(g)
    SET r.preference = 'cultural'
    """, name=name, email=email, audience=audience, privated=privated, isClosed=isClosed,
         tripType=tripType, departureDate=departureDate, returnDate=returnDate)

with driver.session() as session:
    print("Conectado a Neo4j")

    # Crear 10 usuarios
    for i in range(10):
        name = f"Usuario{i+1}"
        email = f"usuario{i+1}@test.com"
        session.execute_write(create_user, name, email)

    # Crear 5 grupos públicos
    for i in range(5):
        name = f"GrupoPublico{i+1}"
        email = f"usuario{i+1}@test.com"
        audience = random.choice(['Jóvenes', 'Familias', 'Adultos'])
        privated = 'public'
        isClosed = False
        tripType = random.choice(['Playa', 'Cultural', 'Aventura'])

        # Fechas aleatorias (hoy + i días)
        today = datetime.date.today()
        departureDate = today + datetime.timedelta(days=i*5)
        returnDate = departureDate + datetime.timedelta(days=7)

        session.execute_write(create_public_group, name, email, audience, privated, isClosed, tripType, departureDate.isoformat(), returnDate.isoformat())

driver.close()
