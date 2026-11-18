package fr.diginamic.recensement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
public class DaoRecensementTest {

    @Test
    void testRechercheEntitesEtVillesDeMemeNom() {
        try (EntityManagerFactory factory = Persistence.createEntityManagerFactory("recensement-jpa");
             EntityManager em = factory.createEntityManager()) {
            RegionDao regionDao = new RegionDao(em);
            DepartementDao departementDao = new DepartementDao(em);
            VilleDao villeDao = new VilleDao(em);
            EntityTransaction transaction = em.getTransaction();

            try {
                transaction.begin();

                // Crée deux villes de même nom dans deux départements différents.
                Region region = new Region("T2", "Région de test DAO");
                Departement premierDepartement = new Departement("T02");
                Departement secondDepartement = new Departement("T03");
                Ville premiereVille = new Ville("Ville de test", 1234);
                Ville secondeVille = new Ville("Ville de test", 5678);
                region.ajouterDepartement(premierDepartement);
                region.ajouterDepartement(secondDepartement);
                premierDepartement.ajouterVille(premiereVille);
                secondDepartement.ajouterVille(secondeVille);

                regionDao.insert(region);
                departementDao.insert(premierDepartement);
                departementDao.insert(secondDepartement);
                villeDao.insert(premiereVille);
                villeDao.insert(secondeVille);
                em.flush();
                em.clear();

                // Recherche les objets enregistrés avec les DAO.
                Region regionLue = regionDao.findByCode("T2");
                Departement premierLu = departementDao.findByCode("T02");
                Departement secondLu = departementDao.findByCode("T03");
                assertNotNull(regionLue);
                assertNotNull(premierLu);
                assertNotNull(secondLu);
                assertEquals(region.getId(), regionLue.getId());
                assertEquals(premierDepartement.getId(), premierLu.getId());
                assertEquals(secondDepartement.getId(), secondLu.getId());
                assertEquals(regionLue, regionDao.findById(regionLue.getId()));
                assertEquals(premierLu, departementDao.findById(premierLu.getId()));

                Ville premiereLue = villeDao.findByNomAndDepartement("Ville de test", premierLu);
                Ville secondeLue = villeDao.findByNomAndDepartement("Ville de test", secondLu);
                assertNotNull(premiereLue);
                assertNotNull(secondeLue);
                assertEquals(premiereVille.getId(), premiereLue.getId());
                assertEquals(secondeVille.getId(), secondeLue.getId());
                assertEquals(1234, premiereLue.getPopulation());
                assertEquals(5678, secondeLue.getPopulation());

                // Vérifie les listes et les recherches sans résultat.
                assertTrue(regionDao.findAll().contains(regionLue));
                assertTrue(departementDao.findAll().contains(premierLu));
                assertTrue(villeDao.findAll().contains(premiereLue));
                assertNull(regionDao.findByCode("ABS"));
                assertNull(departementDao.findByCode("ABS"));
                assertNull(villeDao.findByNomAndDepartement("Ville absente", premierLu));
            } finally {
                // Ne garde aucune donnée de test en base.
                if (transaction.isActive()) {
                    transaction.rollback();
                }
            }
        }
    }
}
