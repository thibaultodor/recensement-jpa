package fr.diginamic.recensement.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "REGION")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @Column(name = "CODE", nullable = false, unique = true, length = 3)
    private String code;

    @Column(name = "NOM", nullable = false, length = 100)
    private String nom;

    // Une région possède plusieurs départements.
    // "region" est le nom du champ dans Departement.
    @OneToMany(mappedBy = "region")
    private List<Departement> departements = new ArrayList<>();

    public Region() {
    }

    public Region(String code, String nom) {
        this.code = code;
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Departement> getDepartements() {
        return departements;
    }

    public void ajouterDepartement(Departement departement) {
        departement.setRegion(this);
    }
}
