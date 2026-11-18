package fr.diginamic.recensement.dao;

import fr.diginamic.recensement.entites.Departement;
import fr.diginamic.recensement.entites.Ville;
import jakarta.persistence.EntityManager;

import java.util.List;

/** Accès aux villes en base. */
public class VilleDao {

    private final EntityManager em;

    public VilleDao(EntityManager em) {
        this.em = em;
    }

    public void insert(Ville ville) {
        em.persist(ville);
    }

    public void delete(Ville ville) {
        em.remove(ville);
    }

    public Ville findById(int id) {
        return em.find(Ville.class, id);
    }

    public Ville findByNomAndDepartement(String nom, Departement departement) {
        // Deux villes peuvent porter le même nom dans des départements différents.
        List<Ville> villes = em.createQuery("""
                        SELECT v FROM Ville v
                        WHERE v.nom = :nom AND v.departement = :departement
                        """, Ville.class)
                .setParameter("nom", nom)
                .setParameter("departement", departement)
                .getResultList();

        if (villes.isEmpty()) {
            return null;
        }
        return villes.get(0);
    }

    public List<Ville> findAll() {
        return em.createQuery("SELECT v FROM Ville v", Ville.class).getResultList();
    }

    // Trie les villes de la plus peuplée à la moins peuplée.
    public List<Ville> findAllOrderByPopulationDesc() {
        return em.createQuery(
                "SELECT v FROM Ville v ORDER BY v.population DESC, v.nom ASC", Ville.class)
                .getResultList();
    }

    public List<Ville> findByDepartementOrderByPopulationDesc(String codeDepartement) {
        return em.createQuery("""
                        SELECT v FROM Ville v
                        WHERE v.departement.code = :code
                        ORDER BY v.population DESC, v.nom ASC
                        """, Ville.class)
                .setParameter("code", codeDepartement)
                .getResultList();
    }

    public List<Ville> findByRegionOrderByPopulationDesc(String codeRegion) {
        return em.createQuery("""
                        SELECT v FROM Ville v
                        WHERE v.departement.region.code = :code
                        ORDER BY v.population DESC, v.nom ASC
                        """, Ville.class)
                .setParameter("code", codeRegion)
                .getResultList();
    }

    // BETWEEN inclut les deux bornes.
    public List<Ville> findByPopulationBetween(int min, int max) {
        return em.createQuery("""
                        SELECT v FROM Ville v
                        WHERE v.population BETWEEN :min AND :max
                        ORDER BY v.population DESC, v.nom ASC
                        """, Ville.class)
                .setParameter("min", min)
                .setParameter("max", max)
                .getResultList();
    }

    public List<Ville> findByDepartementAndPopulationBetween(String codeDepartement, int min, int max) {
        return em.createQuery("""
                        SELECT v FROM Ville v
                        WHERE v.departement.code = :code
                          AND v.population BETWEEN :min AND :max
                        ORDER BY v.population DESC, v.nom ASC
                        """, Ville.class)
                .setParameter("code", codeDepartement)
                .setParameter("min", min)
                .setParameter("max", max)
                .getResultList();
    }

    public List<Ville> findByRegionAndPopulationBetween(String codeRegion, int min, int max) {
        return em.createQuery("""
                        SELECT v FROM Ville v
                        WHERE v.departement.region.code = :code
                          AND v.population BETWEEN :min AND :max
                        ORDER BY v.population DESC, v.nom ASC
                        """, Ville.class)
                .setParameter("code", codeRegion)
                .setParameter("min", min)
                .setParameter("max", max)
                .getResultList();
    }
}
