package fr.diginamic.recensement.dao;

import fr.diginamic.recensement.entites.Region;
import jakarta.persistence.EntityManager;

import java.util.List;

/** Accès aux régions en base. */
public class RegionDao {

    private final EntityManager em;

    public RegionDao(EntityManager em) {
        this.em = em;
    }

    public void insert(Region region) {
        em.persist(region);
    }

    public Region findById(int id) {
        return em.find(Region.class, id);
    }

    public Region findByCode(String code) {
        List<Region> regions = em.createQuery(
                        "SELECT r FROM Region r WHERE r.code = :code", Region.class)
                .setParameter("code", code)
                .getResultList();

        // Renvoie null si la région n'existe pas.
        if (regions.isEmpty()) {
            return null;
        }
        return regions.get(0);
    }

    public List<Region> findAll() {
        return em.createQuery("SELECT r FROM Region r", Region.class).getResultList();
    }
}
