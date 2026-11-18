package fr.diginamic.recensement;

import fr.diginamic.recensement.csv.LecteurRecensement;
import fr.diginamic.recensement.csv.LigneRecensement;
import fr.diginamic.recensement.service.ImportRecensementService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class IntegrationRecensement {

    public static void main(String[] args) throws IOException {
        // Permet de calculer la duré e de l'import.
        long debut = System.currentTimeMillis();

        // Lit les communes avec le nom de leur département.
        LecteurRecensement lecteur = new LecteurRecensement();
        List<LigneRecensement> lignes = lecteur.read(Path.of("data", "Communes.csv"), Path.of("data", "Departements.csv"));

        // Masque le SQL pour ne pas afficher des milliers d'insertions.
        try (EntityManagerFactory factory = Persistence.createEntityManagerFactory("recensement-jpa", Map.of("hibernate.show_sql", "false")); EntityManager em = factory.createEntityManager()) {
            EntityTransaction transaction = em.getTransaction();
            try {
                transaction.begin();
                ImportRecensementService service = new ImportRecensementService(em);
                int villesAjoutees = service.importData(lignes);
                transaction.commit();

                System.out.println("Import terminé : " + villesAjoutees + " villes ajoutées.");
                System.out.println("Durée : " + (System.currentTimeMillis() - debut) + " ms.");
            } catch (RuntimeException erreur) {
                // Annule l'import en cas d'erreur.
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                System.err.println("Import annulé après " + (System.currentTimeMillis() - debut) + " ms.");
                System.err.println("Erreur : " + erreur.getMessage());
                throw erreur;
            }
        }
    }
}
