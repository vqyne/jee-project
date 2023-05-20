# Gestion des épreuves des Jeux Olympiques de Paris 2024

Ce projet consiste à développer un site web pour la gestion des épreuves des Jeux Olympiques de Paris 2024. Le site permettra de gérer les sites de compétition, les disciplines, les sessions d'épreuves et les athlètes.

## Fonctionnalités

### Gestion des sites de compétition

- Affichage de la liste des sites
- Création et édition des sites
- Un site est caractérisé par :
  - Nom
  - Ville
  - Catégorie (stade, salle de spectacle, lieu public, centre aquatique)

### Gestion des disciplines

- Affichage de la liste des disciplines
- Création et édition des disciplines
- Une discipline est caractérisée par :
  - Nom
  - Flag indiquant si la discipline est présente aux jeux paralympiques (oui/non)

### Gestion des sessions d'épreuves

- Affichage de la liste des sessions
- Création et édition des sessions
- Une session est caractérisée par :
  - Code de session
  - Date
  - Heure de début
  - Heure de fin
  - Discipline
  - Site de compétition
  - Description
  - Type de session (qualifications/médailles)
  - Catégorie (H/F/mixte)

### Gestion des athlètes

- Importation d'un fichier CSV contenant la liste des athlètes (Nom, prénom, pays, discipline, etc.)

### Statistiques

- Classement des 5 sites ayant le plus de sessions
- Classement des 5 disciplines ayant le temps d'épreuves le plus long

### Gestion des droits d'accès

- Accès protégé par login/password
- Profils d'utilisateur :
  - Gestionnaire compétitions, sites et disciplines
  - Gestionnaire sessions
  - Admin

### Version grand public

- Consultation des sessions avec affichage des informations détaillées
- Affichage de la liste des athlètes
- Pagination de l'affichage de la liste des athlètes (optionnel)

## Prérequis

- Java
- Serveur web (Apache Tomcat, Jetty, etc.)
- Base de données (MySQL, PostgreSQL, etc.)

## Installation

1. Cloner le dépôt ( https://github.com/vqyne/jee-project.git )
2. Configurer la base de données dans le fichier `src/main/java/config.properties`
3. Compiler et déployer le projet sur le serveur web
4. Accéder au site via l'URL fournie par le serveur

## Auteurs

- Loueï BEKHEDDA
- Gurwan DELAUNAY
- Oussama KHALOUI
- Thomas CROIZET
