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
}
