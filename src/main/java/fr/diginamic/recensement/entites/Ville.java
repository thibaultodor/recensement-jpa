package fr.diginamic.recensement.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "VILLE")
public class Ville {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "NOM", nullable = false, length = 255)
    private String nom;

    @Column(name = "POPULATION", nullable = false)
    private int population;

    // Plusieurs villes appartiennent à un département.
    @ManyToOne
    @JoinColumn(name = "ID_DEPARTEMENT", nullable = false)
    private Departement departement;

    public Ville() {
    }

    public Ville(String nom, int population) {
        this.nom = nom;
        this.population = population;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        // Met à jour la ville et la liste de son département.
        if (this.departement == departement) {
            return;
        }
        if (this.departement != null) {
            this.departement.getVilles().remove(this);
        }
        this.departement = departement;
        if (departement != null && !departement.getVilles().contains(this)) {
            departement.getVilles().add(this);
        }
    }
}
