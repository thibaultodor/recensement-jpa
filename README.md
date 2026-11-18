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