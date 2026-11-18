# Recensement JPA

TP 07 : créer une application Java utilisant JPA pour importer des données de
recensement de population dans une base de données nommée `recensement`.

[Consulter l’énoncé du TP 07 (PDF)](docs/tp-jpa-07-recensement.pdf).

## Travail à réaliser

- Créer les entités `Region` (id, code, nom), `Departement` (id, code) et
  `Ville` (id, nom, population).
- Définir les relations : une région possède plusieurs départements et un
  département possède plusieurs villes.
- Générer les tables à partir des entités JPA.
- Créer une classe exécutable `IntegrationRecensement` pour lire le fichier
  des communes et enregistrer les données en base.
- Permettre de relancer l'import sans créer de doublons, en vérifiant les données
  existantes et en veillant aux performances.
- Regrouper les accès à la base dans des DAO : aucune requête en dehors des DAO.

En complément facultatif, `RechercheRecensement` permettra d'effectuer des
recherches JPQL : classement des villes par population, filtres par département
ou région, populations cumulées et recherches entre deux seuils de population.

## Données fournies

Les fichiers sont conservés dans le dossier `data` :

- `Communes.csv` : codes et noms des régions, codes des départements et communes,
  noms des communes et populations municipale, comptée à part et totale.
- `Departements.csv` : correspondance entre les codes et les noms des départements.

Ces CSV sont en UTF-8, séparés par des points-virgules et comportent une ligne
d'en-tête. Le fichier de recensement fourni est nommé `Communes.csv`.

## Étape 1 : connexion JPA

Le projet Maven est configuré pour Java 17, Hibernate et MariaDB/XAMPP.
Cette étape vérifie uniquement la connexion : aucune entité ni import CSV n'est encore implémenté.

Pour lancer dans IntelliJ :

1. Démarrer le module **MySQL** dans XAMPP.
2. Recharger le projet comme projet Maven via son `pom.xml`.
3. Sélectionner un JDK 17 ou supérieur.
4. Si la base n'existe pas encore, exécuter `sql/01-creation-base.sql`.
5. Ouvrir `src/test/java/fr/diginamic/recensement/ConnexionJpaTest.java`.
6. Cliquer sur le triangle vert à côté de la classe pour lancer le test JUnit.

Le résultat attendu est **1 test réussi**. Il s'agit d'un test d'intégration :
il vérifie une connexion à la vraie base MariaDB, sans modifier de données.
Le tag JUnit `integration` indique sa catégorie ; il est exécuté normalement avec `mvn test`.

La connexion locale utilise le port `3306`, le compte `root` et un mot de passe vide.
Ces paramètres se trouvent dans `src/main/resources/META-INF/persistence.xml`.

Avec Java et Maven configurés, le test se lance avec `mvn test`.

L'organisation des sources suit les conventions Maven :

```text
src/main/java        : code de l'application (à venir)
src/main/resources   : configuration JPA
src/test/java        : tests JUnit
src/test/resources   : ressources propres aux tests, si nécessaire
```

La configuration JPA reste dans `src/main/resources` : le test réutilise celle
qui servira à l'application. Le dossier `data` conserve les CSV à importer plus tard.
Après modification du `pom.xml`, recharger Maven dans IntelliJ pour actualiser
les dépendances et les dossiers sources. Il ne faut pas marquer tout `src` comme source.
