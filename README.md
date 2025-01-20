# Application de vente d'objets entre particuliers

![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-%23005C0F.svg?style=for-the-badge&logo=Thymeleaf&logoColor=white)
![HTML5](https://img.shields.io/badge/html5-%23E34F26.svg?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/css3-%231572B6.svg?style=for-the-badge&logo=css3&logoColor=white)
![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E)


## Description du projet

Dans le cadre de la ressource **“Programmation avancée”**, nous avons conçu et développé une application web innovante dédiée à la vente d’objets entre particuliers. 

L’objectif principal était de créer une plateforme simple d’utilisation, fonctionnelle et sécurisée, capable d’offrir une expérience fluide et intuitive aux utilisateurs.

## Fonctionnalités principales

1. **Authentification :**
   - Page d’accueil avec système d’inscription et de connexion pour les utilisateurs.

2. **Tableau de bord utilisateur :**
   - Accès aux objets mis en vente par la communauté.
   - Consultation des objets que l’utilisateur a mis en vente.
   - Liste des objets vendus par l’utilisateur.

3. **Recherche optimisée :**
   - Barre de recherche pour filtrer les objets disponibles en fonction de mots-clés.
   - Les résultats affichent uniquement les articles encore en stock.

4. **Section administrateur :**
   - Consultation du chiffre d’affaires total généré par la plateforme.
   - Calcul automatique de la commission de 10 % prélevée sur chaque vente.

5. **Sécurité et performances :**
   - Gestion sécurisée des mots de passe.
   - Chiffrement des échanges HTTP.
   - Architecture modulaire et évolutive.

## Architecture technique

- **Framework utilisé :** Spring (Spring Boot), garantissant une base robuste pour le développement web.
- **Base de données :** MariaDB, permettant une gestion efficace et fiable des données.
- **Tests automatisés :** Réalisés avec **Mockito**, ces tests valident chaque fonctionnalité critique et assurent la robustesse du code.


## Configuration requise

1. **Connexion à la base de données :**  
   - Les propriétés de connexion à la base de données (dans le fichier `application.properties`) doivent être configurées selon vos identifiants et paramètres. Exemple :
     ```properties
     spring.datasource.url=jdbc:mariadb://localhost:3306/votre_base_de_donnees
     spring.datasource.username=VOTRE_UTILISATEUR
     spring.datasource.password=VOTRE_MOT_DE_PASSE
     ```
   - Remplacez `VOTRE_UTILISATEUR` et `VOTRE_MOT_DE_PASSE` par vos propres identifiants.

2. **Outils nécessaires :**
   - Java (version 17 ou supérieure).
   - Maven pour la gestion des dépendances et le build du projet.
   - Un serveur MariaDB fonctionnel.


## Sécurité et tests

- **Sécurité :**
  - Les mots de passe des utilisateurs sont sécurisés par un mécanisme de hachage robuste.
  - Toutes les communications entre le client et le serveur sont protégées par le chiffrement HTTPS.

- **Tests :**
  - Les tests unitaires et d'intégration ont été développés avec **Mockito** pour garantir une couverture de code optimale.
  - Chaque fonctionnalité critique a été testée pour respecter les spécifications et prévenir les régressions.


## Avantages et perspectives

Avec cette plateforme, nous proposons un outil moderne et fiable, idéal pour un usage quotidien. Grâce à son architecture modulaire, l'application peut être facilement adaptée à des contextes professionnels, ouvrant la voie à de nouveaux usages et opportunités de développement.

---

## Lancement du projet

1. Clonez le dépôt :
   ```bash
   git clone [URL_DU_DEPOT]
   cd [NOM_DU_PROJET]
