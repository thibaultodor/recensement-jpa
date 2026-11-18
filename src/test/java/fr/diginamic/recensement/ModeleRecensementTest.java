package fr.diginamic.recensement;

import static org.junit.jupiter.api.Assertions.assertEquals;

import fr.diginamic.recensement.dao.DepartementDao;
import fr.diginamic.recensement.dao.RegionDao;
import fr.diginamic.recensement.dao.VilleDao;
import fr.diginamic.recensement.entites.Departement;
import fr.diginamic.recensement.entites.Region;
import fr.diginamic.recensement.entites.Ville;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration")
public class ModeleRecensementTest {

    @Test
    void testEnregistrementVilleAvecDepartementEtRegion() {
        try (EntityManagerFactory factory = Persistence.createEntityManagerFactory("recensement-jpa");
             EntityManager em = factory.createEntityManager()) {
            // Les DAO partagent le même EntityManager.
            RegionDao regionDao = new RegionDao(em);
            DepartementDao departementDao = new DepartementDao(em);
            VilleDao villeDao = new VilleDao(em);
            EntityTransaction transaction = em.getTransaction();
            try {
                transaction.begin();

                // Crée une région, un département et une ville de test.
                Region region = new Region("T1", "Région de test");
                Departement departement = new Departement("T01");
                Ville ville = new Ville("Ville de test", 1234);

                // Relie les trois objets.
                region.ajouterDepartement(departement);
                departement.ajouterVille(ville);

                // Enregistre les objets en base.
                regionDao.insert(region);
                departementDao.insert(departement);
                villeDao.insert(ville);

                // Écrit en base, puis recharge les objets.
                em.flush();
                int idVille = ville.getId();
                em.clear();

                // Recharge la ville depuis la base.
                Ville villeLue = villeDao.findById(idVille);

                // Vérifie les données et les relations.
                assertEquals("Ville de test", villeLue.getNom());
                assertEquals(1234, villeLue.getPopulation());
                assertEquals("T01", villeLue.getDepartement().getCode());
                assertEquals("T1", villeLue.getDepartement().getRegion().getCode());
                assertEquals(1, villeLue.getDepartement().getVilles().size());
                assertEquals(1, villeLue.getDepartement().getRegion().getDepartements().size());
            } finally {
                // Annule les insertions pour ne pas garder les données du test.
                if (transaction.isActive()) {
                    transaction.rollback();
                }
            }
        }
    }
}
