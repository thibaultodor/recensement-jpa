package fr.diginamic.recensement.service;

import fr.diginamic.recensement.csv.LigneRecensement;
import fr.diginamic.recensement.dao.DepartementDao;
import fr.diginamic.recensement.dao.RegionDao;
import fr.diginamic.recensement.dao.VilleDao;
import fr.diginamic.recensement.entites.Departement;
import fr.diginamic.recensement.entites.Region;
import fr.diginamic.recensement.entites.Ville;
import jakarta.persistence.EntityManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ImportRecensementService {

    private final RegionDao regionDao;
    private final DepartementDao departementDao;
    private final VilleDao villeDao;

    public ImportRecensementService(EntityManager em) {
        regionDao = new RegionDao(em);
        departementDao = new DepartementDao(em);
        villeDao = new VilleDao(em);
    }

    public int importData(List<LigneRecensement> lignes) {
        // Charge les données existantes pour éviter les recherches à chaque ligne.
        Map<String, Region> regions = new HashMap<>();
        for (Region region : regionDao.findAll()) {
            regions.put(region.getCode(), region);
        }

        Map<String, Departement> departements = new HashMap<>();
        for (Departement departement : departementDao.findAll()) {
            departements.put(departement.getCode(), departement);
        }

        Set<String> villesExistantes = new HashSet<>();
        for (Ville ville : villeDao.findAll()) {
            villesExistantes.add(ville.getDepartement().getCode() + ";" + ville.getNom());
        }

        int villesAjoutees = 0;
        for (LigneRecensement ligne : lignes) {
            // Crée la région si elle n'existe pas encore.
            Region region = regions.get(ligne.getCodeRegion());
            if (region == null) {
                region = new Region(ligne.getCodeRegion(), ligne.getNomRegion());
                regionDao.insert(region);
                regions.put(region.getCode(), region);
            }

            // Crée le département et le rattache à sa région.
            Departement departement = departements.get(ligne.getCodeDepartement());
            if (departement == null) {
                departement = new Departement(ligne.getCodeDepartement());
                region.ajouterDepartement(departement);
                departementDao.insert(departement);
                departements.put(departement.getCode(), departement);
            }

            // Le département et le nom permettent de repérer une ville déjà importée.
            String cleVille = ligne.getCodeDepartement() + ";" + ligne.getNomCommune();
            if (!villesExistantes.contains(cleVille)) {
                Ville ville = new Ville(ligne.getNomCommune(), ligne.getPopulationTotale());
                departement.ajouterVille(ville);
                villeDao.insert(ville);
                villesExistantes.add(cleVille);
                villesAjoutees++;
            }
        }

        return villesAjoutees;
    }
}
