package fr.diginamic.recensement;

import static org.junit.jupiter.api.Assertions.assertEquals;

import fr.diginamic.recensement.csv.LigneRecensement;
import fr.diginamic.recensement.service.ImportRecensementService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

@Tag("integration")
public class ImportRecensementTest {

    @Test
    void testImportSansDoublon() {
        try (EntityManagerFactory factory = Persistence.createEntityManagerFactory("recensement-jpa");
             EntityManager em = factory.createEntityManager()) {
            EntityTransaction transaction = em.getTransaction();
            try {
                transaction.begin();
                ImportRecensementService service = new ImportRecensementService(em);

                // La même commune apparaît deux fois dans les données de test.
                LigneRecensement ligne = new LigneRecensement(
                        "T3", "Région de test import", "T04", "001", "Ville de test import", 1234);
                List<LigneRecensement> lignes = List.of(ligne, ligne);
                assertEquals(1, service.importData(lignes));

                // Recharge les données, puis rejoue l'import.
                em.flush();
                em.clear();
                assertEquals(0, service.importData(lignes));
            } finally {
                // Ne garde pas les données du test.
                if (transaction.isActive()) {
                    transaction.rollback();
                }
            }
        }
    }
}
