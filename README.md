SafetyNet Alerts - Java / Spring Boot
Bienvenue dans le projet SafetyNet Alerts, une application en Java/Spring Boot qui fournit des alertes de sécurité en fonction des données de personnes, casernes et dossiers médicaux.

Structure du projet (MVC)
Le projet suit l’architecture MVC avec les packages suivants :

controller : reçoit les requêtes HTTP (REST API)

service : contient la logique métier (traitement, règles)

repository : gère l’accès aux données (JSON, en mémoire, etc.)

model : contient les entités (Person, FireStation, MedicalRecord...)

Convention de nommage
Afin d’assurer une cohérence entre la base de données, les classes Java, les endpoints REST et le JSON, voici les règles de nommage adoptées :

Classe Java : nom au singulier, en PascalCase
Exemple : Person

Table SQL : nom au pluriel, en snake_case (forcé avec @Table)
Exemple : @Table(name = "persons")

Champ Java : en camelCase
Exemple : firstName

Colonne SQL : en snake_case, forçé avec @Column
Exemple : @Column(name = "first_name")

JSON : champ racine au pluriel s’il contient une collection
Exemple : "persons": [ ... ]

Endpoint REST :

Pour récupérer tous les éléments : nom au pluriel
Exemple : /persons

Pour accéder à un élément unique : /persons/{id}

Cette cohérence permet d'éviter les erreurs de désérialisation JSON, les conflits entre noms de colonnes, et rend l’API plus claire.

Technologies utilisées
Java 17+

Spring Boot

Hibernate / JPA

Jackson (pour la gestion JSON)

MySQL

Maven

Lancer le projet
Cloner le projet depuis GitHub : git clone https://github.com/votre-nom/safetynet-alerts.git

Lancer l'application avec Maven : mvn spring-boot:run

Exemple de requête HTTP : GET http://localhost:8080/persons