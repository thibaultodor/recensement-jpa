package fr.diginamic.recensement.dao;

import fr.diginamic.recensement.entites.Departement;
import jakarta.persistence.EntityManager;

import java.util.List;

/** Accès aux départements en base. */
public class DepartementDao {

    private final EntityManager em;

    public DepartementDao(EntityManager em) {
        this.em = em;
    }

    public void insert(Departement departement) {
        em.persist(departement);
    }

    public Departement findById(int id) {
        return em.find(Departement.class, id);
    }

    public Departement findByCode(String code) {
        List<Departement> departements = em.createQuery(
                        "SELECT d FROM Departement d WHERE d.code = :code", Departement.class)
                .setParameter("code", code)
                .getResultList();

        // Renvoie null si le département n'existe pas.
        if (departements.isEmpty()) {
            return null;
        }
        return departements.get(0);
    }

    public List<Departement> findAll() {
        return em.createQuery("SELECT d FROM Departement d", Departement.class).getResultList();
    }

    // Additionne les populations des villes du département.
    public Long findPopulationByCode(String code) {
        return em.createQuery("""
                        SELECT SUM(v.population) FROM Departement d
                        JOIN d.villes v
                        WHERE d.code = :code
                        GROUP BY d.code
                        """, Long.class)
                .setParameter("code", code)
                .getSingleResultOrNull();
    }
}
