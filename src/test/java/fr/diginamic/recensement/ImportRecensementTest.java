package fr.diginamic.recensement;

import static org.junit.jupiter.api.Assertions.assertEquals;

import fr.diginamic.recensement.csv.LigneRecensement;
import fr.diginamic.recensement.dao.DepartementDao;
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
                        "T3", "Région de test import", "T04", "Département de test", "001", "Ville de test import", 1234);
                List<LigneRecensement> lignes = List.of(ligne, ligne);
                assertEquals(1, service.importData(lignes));
                DepartementDao departementDao = new DepartementDao(em);
                assertEquals("Département de test", departementDao.findByCode("T04").getNom());

                // Rejoue l'import avec un nouveau nom de département.
                em.flush();
                em.clear();
                LigneRecensement ligneModifiee = new LigneRecensement(
                        "T3", "Région de test import", "T04", "Nouveau nom", "001", "Ville de test import", 1234);
                assertEquals(0, service.importData(List.of(ligneModifiee)));
                em.flush();
                em.clear();
                assertEquals("Nouveau nom", departementDao.findByCode("T04").getNom());
            } finally {
                // Ne garde pas les données du test.
                if (transaction.isActive()) {
                    transaction.rollback();
                }
            }
        }
    }
}
