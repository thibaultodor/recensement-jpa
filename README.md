# Recensement JPA

TP 07 : importer des données de recensement dans MariaDB avec JPA et Hibernate,
puis effectuer des recherches JPQL.

[Consulter l’énoncé du TP 07 (PDF)](docs/tp-jpa-07-recensement.pdf).

Les entités `Region`, `Departement` et `Ville` représentent les données : une région
possède plusieurs départements et un département possède plusieurs villes.
JPA génère les tables et les accès à la base sont regroupés dans les DAO.
L'import peut être relancé sans créer de doublons.

## Lancer le projet

Prérequis : Java 17, Maven et MariaDB démarrée dans XAMPP.

Créer la base `recensement`, puis vérifier les paramètres de connexion dans
[persistence.xml](src/main/resources/META-INF/persistence.xml).

Dans IntelliJ, lancer les méthodes `main` dans cet ordre :

1. [IntegrationRecensement](src/main/java/fr/diginamic/recensement/IntegrationRecensement.java) : importer les données.
2. [RechercheRecensement](src/main/java/fr/diginamic/recensement/RechercheRecensement.java) : afficher les classements, filtres et populations cumulées.

Les codes de département et de région ainsi que les seuils de population se
modifient au début de `RechercheRecensement`. L'affichage est limité aux dix
premières villes de chaque liste.

## Données fournies

Les fichiers sont conservés dans [data](data), en UTF-8 avec des points-virgules :

- `Communes.csv` : régions, départements, communes et populations.
- `Departements.csv` : correspondance entre les codes et les noms des départements.

La population utilisée est la **population totale** du fichier.

## Tests

Depuis la racine du projet :

```shell
mvn test
```

- `LecteurRecensementTest` : lecture des communes, données de Montpellier et regroupement de Lyon sans changer la population totale.
- `LecteurDepartementsTest` : correspondance entre les codes et les noms des départements.
- `ConnexionJpaTest` : connexion réelle à MariaDB.
- `ModeleRecensementTest` : enregistrement et relecture des entités avec leurs relations.
- `DaoRecensementTest` : recherches et distinction de villes de même nom dans deux départements.
- `ImportRecensementTest` : import sans doublon et mise à jour du nom d'un département.

Les lecteurs sont testés sans base. Les autres tests sont des tests d'intégration :
MariaDB doit être démarrée. Les données créées pendant ces tests sont annulées
par un `rollback`.

## Choix réalisés

- **Noms des départements** : ajout d'un champ `nom`, renseigné grâce à
  `Departements.csv` dès la lecture des communes.
- **Lyon** : le fichier contient neuf arrondissements. Ils sont regroupés en une
  seule ville en additionnant leurs populations, soit 523 164 habitants dans ce
  jeu de données. Les anciennes entrées sont retirées à l'import pour éviter un
  double comptage. Le CSV d'origine est conservé.
- **Cache L2** : seules les régions et les départements sont mis en cache avec
  Ehcache. Ces données sont peu nombreuses et partagées par beaucoup de villes,
  ce qui limite la mémoire utilisée. Les entrées sont conservées au maximum
  30 minutes et partagées entre les `EntityManager` d'une même
  `EntityManagerFactory`. Le cache est vidé à sa fermeture.
