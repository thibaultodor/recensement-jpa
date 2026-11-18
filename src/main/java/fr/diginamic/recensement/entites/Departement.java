package fr.diginamic.recensement.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DEPARTEMENT")
public class Departement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    // Un texte permet de stocker aussi "2A", "2B" ou "971".
    @Column(name = "CODE", nullable = false, unique = true, length = 3)
    private String code;

    // Plusieurs départements appartiennent à une région.
    @ManyToOne
    @JoinColumn(name = "ID_REGION", nullable = false)
    private Region region;

    // Un département possède plusieurs villes.
    @OneToMany(mappedBy = "departement")
    private List<Ville> villes = new ArrayList<>();

    public Departement() {
    }

    public Departement(String code) {
        this.code = code;
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

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        // Met à jour le département et la liste de sa région.
        if (this.region == region) {
            return;
        }
        if (this.region != null) {
            this.region.getDepartements().remove(this);
        }
        this.region = region;
        if (region != null && !region.getDepartements().contains(this)) {
            region.getDepartements().add(this);
        }
    }

    public List<Ville> getVilles() {
        return villes;
    }

    public void ajouterVille(Ville ville) {
        ville.setDepartement(this);
    }
}
