# SpringMongoDB

***

## Indice
1.[Descrizione](#descrizione)
2.[Installazione](#installazione)
3.[Tecnologie](#tecnologie)

***

## Descrizione

L'applicazione Skeleton, si basa su tecnologia Spring/SpringBoot. Sviluppata con la versione Java 17, l'Applicazione e' stata creata per interfacciarsi con un Database
non-relazionale (MongoDB) attraverso l'utilizzo del modulo di Spring dedicato: Spring Data MongoDB al fine di gestire la profilazione Utente per un eventuale gioco Multiplayer.
Inoltre è possibile in maniera "simulata" avviare il gioco, terminarlo, recuperando in questo modo tutte le informazioni e statistiche di gioco.
Tramite l'implementazione di Swagger e servizi REST è possibile interrogare ed interfacciarsi con il db Mongo che consta di due Document:

- Player
- Game

Sfruttando Spring data MongoDb  è possibile l'interrogazione del Document Game al fine i recuperare tutte le statistiche di gioco fin'ora prodotte.


***
## Installazione

- Installazione Java versione 17
- Clonazione del progetto: git clone (https://github.com/gpelosi-dgsspa/SpringMongoDB.git)
- Avviare l'applicativo
- Ai fini dell'interrogazione del db, le chiamate vengono gestite tramite Swagger all'URL: (http://localhost:8080/swagger-ui/index.html)

***
## Tecnologie

1. Java 17
2. SpringBoot 3.2.2
3. MongoDb
4. Spring DataMongoDb
***