package fr.diginamic.recensement;

import fr.diginamic.recensement.dao.DepartementDao;
import fr.diginamic.recensement.dao.RegionDao;
import fr.diginamic.recensement.dao.VilleDao;
import fr.diginamic.recensement.entites.Departement;
import fr.diginamic.recensement.entites.Region;
import fr.diginamic.recensement.entites.Ville;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;
import java.util.Map;

public class RechercheRecensement {

    public static void main(String[] args) {
        // Exemples : Hérault et Occitanie.
        String codeDepartement = "34";
        String codeRegion = "76";
        int min = 100000;
        int max = 300000;

        try (EntityManagerFactory factory = Persistence.createEntityManagerFactory(
                "recensement-jpa", Map.of("hibernate.show_sql", "false"));
             EntityManager em = factory.createEntityManager()) {
            VilleDao villeDao = new VilleDao(em);
            DepartementDao departementDao = new DepartementDao(em);
            RegionDao regionDao = new RegionDao(em);

            Departement departement = departementDao.findByCode(codeDepartement);
            String libelleDepartement = departement == null ? codeDepartement : formatDepartement(departement);

            afficherVilles("Villes par population décroissante - France",
                    villeDao.findAllOrderByPopulationDesc());
            afficherVilles("Villes par population décroissante - département " + libelleDepartement,
                    villeDao.findByDepartementOrderByPopulationDesc(codeDepartement));
            afficherVilles("Villes par population décroissante - région " + regionDao.findByCode(codeRegion).getNom(),
                    villeDao.findByRegionOrderByPopulationDesc(codeRegion));

            System.out.println("\nPopulation du département " + libelleDepartement + " : "
                    + departementDao.findPopulationByCode(codeDepartement));
            System.out.println("Population de la région " + regionDao.findByCode(codeRegion).getNom() + " : "
                    + regionDao.findPopulationByCode(codeRegion));

            afficherVilles("Villes entre " + min + " et " + max + " habitants - France",
                    villeDao.findByPopulationBetween(min, max));
            afficherVilles("Villes entre " + min + " et " + max + " habitants - département " + libelleDepartement,
                    villeDao.findByDepartementAndPopulationBetween(codeDepartement, min, max));
            afficherVilles("Villes entre " + min + " et " + max + " habitants - région " + regionDao.findByCode(codeRegion).getNom(),
                    villeDao.findByRegionAndPopulationBetween(codeRegion, min, max));
        }
    }

    private static void afficherVilles(String titre, List<Ville> villes) {
        System.out.println("\n" + titre + " (" + villes.size() + " résultats)");

        // Affiche seulement les dix premières villes de chaque liste.
        for (int i = 0; i < Math.min(10, villes.size()); i++) {
            Ville ville = villes.get(i);
            System.out.println(ville.getNom() + " - " + formatDepartement(ville.getDepartement())
                    + " : " + ville.getPopulation() + " habitants");
        }
    }

    private static String formatDepartement(Departement departement) {
        if (departement.getNom() == null) {
            return departement.getCode();
        }
        return departement.getNom() + " (" + departement.getCode() + ")";
    }
}
